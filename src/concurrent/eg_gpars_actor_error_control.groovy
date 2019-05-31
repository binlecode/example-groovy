@Grab(group='org.codehaus.gpars', module='gpars', version='1.2.1')

import groovyx.gpars.actor.Actors
import groovyx.gpars.actor.DefaultActor
import groovyx.gpars.actor.DynamicDispatchActor

import java.util.concurrent.TimeUnit

/**
 * Instead of using try-catch to wrap logic block, an {@code onException()} method can be defined
 * to catch exceptions in aspect oriented way: code is cleaner.
 */
def act = Actors.actor() {
    delegate.metaClass.onException = { ex ->
        println "caught exception: ${ex.message ?: ex}"
    }

    react { String msg ->
        throw new RuntimeException("there is an error")
    }
}

act.send 'test 123'
// The actor's .join() method forces the main thread to wait until the actor thread is completed.
// By default, the Actors.actor {} DSL creates actor using default thread pool, which is a daemon thread pool.
// Since daemon threads doesn't 'retain' main thread, the main thread will exit without waiting for
// daemon threads to finish.
act.join()

/**
 * Now we have above mechanism to make the actor stop nicely.
 * But to keep actor continue working with error, we need Supervising that supports:
 * - Get notified of changes on other actors
 * - Take control over them (stopping, restarting…etc)
 * GPars doesn't have have Supervisor type, but we can create one easily with its
 * {@link groovyx.gpars.actor.DynamicDispatchActor} class.
 */
class Supervisor extends DynamicDispatchActor {

    Supervised supervised
    Class<? extends Supervised> supervisedClass

    void link(Supervised supervised) {
        this.supervised = supervised
        this.supervised.supervisor = this
        this.supervisedClass = supervised.class
    }
}

/**
 * Supervised actor will send the exception message to supervisor
 */
class Supervised extends DefaultActor {
    Supervisor supervisor

    // add onException callback to send error message to supervisor
    void onException(Throwable th) {
        this.supervisor << new SupervisedException(th.message)
    }
}

// now let's create a supervised actor
class SimpleSupervisedActor extends Supervised {
    void act() {
        loop {
            react { String msg ->
                if (msg == 'disconnected') {
                    throw new SupervisedException('no! this is not true')
                } else {
                    println "ok, we are connected with: $msg"
                }
            }
        }
    }
}

class SupervisedException extends RuntimeException {
    SupervisedException(String msg) {
        super(msg)
    }
}

def ssa = new SimpleSupervisedActor().start()

/**
 * Similar to other JVM’s (such as Scala) implementations, to restart an actor is creating a new instance of
 * the same type of the stopped actor.
 * <p>
 * Therefore, it is important to keep the supervised actor stateless. If the supervised actor contains state
 * then it is important to make such state serializable and able to transfer to supervisor for the newly
 * created actor to resume state.
 */
def spv = new Supervisor() {
    void onMessage(String message) {
        supervised << message
    }

    /**
     * this message callback receives exception message from supervised actor
     */
    void onMessage(SupervisedException se) {
        println "SUPERVISED ACTOR DIES WITH MESSAGE: ${se.message}"
        supervised = supervisedClass.newInstance().start()
        // no need to reassign supervisedClass as there's no actor type change
    }
}.start()

spv.link(ssa)

spv << 'sky'
spv << 'ocean'
spv << 'disconnected'
TimeUnit.MILLISECONDS.sleep(500) // give some delay for supervisor to spawn new actor
spv << 'garden'

[spv, ssa]*.join()
