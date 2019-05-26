@Grab(group='org.codehaus.gpars', module='gpars', version='1.2.1')

import groovyx.gpars.actor.Actors
import groovyx.gpars.actor.DefaultActor
import groovyx.gpars.dataflow.Promise
import jdk.nashorn.internal.ir.annotations.Immutable

import java.util.concurrent.TimeUnit

/**
 * Actors.actor DSL creates an actor of {@code DefaultActor} type.
 * <p>
 * Actors share a pool of threads, which are dynamically assigned to actors when the actors need to
 * {@code react} to messages sent to them. The threads are returned back to the pool once a message has been
 * processed and the actor is idle waiting for some more messages to arrive. When idle, actors are
 * detached to the thread pool so a relatively small thread pool can serve potentially unlimited number
 * of actors. Virtually unlimited scalability in number of actors is the main advantage of event-based actors.
 * <p>
 * Under the covers the memory is synchronized each time a thread is assigned to an actor. Therefore, the actor’s
 * state can be safely modified by code in the body without any other extra (synchronization or locking) effort.
 */
def act = Actors.actor({
    // loop ensures that the actor does not stop after having processed the first message
    loop {
        // an actor’s message handler (react) can only expect 0 or 1 argument
        react { msg ->  // make sure the type expected, otherwise you’ll get a cast exception
            println "received $msg"
            TimeUnit.MILLISECONDS.sleep(200) // add some delay to mimic a blocking process
            reply "I've got $msg"
        }
    }

    // use delegate to customize actor lifecycle events callback with groovy meta-programming
    delegate.metaClass {
        afterStop = {
            println 'act is stopped'
        }
        onInterrupt = { InterruptedException ie ->
            println 'act is interrupted'
        }
    }
})

// sendAndWait is a blocking call
println (act.sendAndWait('hey'))

// sendAndContinue is non-blocking with a callback closure
act.sendAndContinue('hola!', { println it })
println 'waiting for reply ...'

// sendAndPromise returns a promise
Promise<String> pms = act.sendAndPromise('promise')
println 'waiting for promise ...'

// .whenBound is non-blocking call that schedules a callback to run when data is available
// note that promise schedules a callback closure to process arrived data in stream mode
// therefore, once the data is claimed by the closure, the .get() method won't be able to retrieve data again
pms.whenBound { println "when bound: $it" }
println pms.get()  // data already drained by callback closure above, thus nothing returns from .get()

// send message to a terminated actor
def oneTimeReceiver = Actors.actor {
    react { msg ->
        println "received $msg"
        println "and that's it"
    }
}
oneTimeReceiver.send('hello once')
TimeUnit.MILLISECONDS.sleep(500)  // after some delay the actor is already terminated
try {
    oneTimeReceiver.send('hello twice')  //
} catch (ex) {
    println "caught exception: ${ex.message}"
    assert ex instanceof IllegalStateException
    assert ex.message == 'The actor cannot accept messages at this point.'
}

// Actors provide a join() method to allow caller thread to wait for the actor to terminate
oneTimeReceiver.join()

// make message object immutable as much as you can (if not always)
@Immutable
class Greeting {
    String message
    String event
}

/**
 * Alternative to GPars dsl, define an Actor class extending `DefaultActor` class, and override the `act()`
 * method.
 */
class Member extends DefaultActor {
    /**
     * Note the protected keyword, act() is internal to actor class, but open for inheritance
     */
    @Override
    protected void act() {
        loop(2) {  // loop accepts a looping number parameter
            react { greeting ->
                println "${greeting.message} in this ${greeting.event}"
//            sender.send "nice to meet you too"  // alternative way to send replies back to 'sender'
                reply 'pleasure'
            }
        }
    }
}

// The new instance (from class constructor call) needs to be started so that it attaches itself to
// the thread pool and can start accepting messages. While in DSL mode, the closure simply defines
// an inline actor instance and starts it.
def member = new Member().start()

println(member.sendAndWait(new Greeting(message: 'good morning', event: 'conference meeting')))
println(member.sendAndWait(new Greeting(message: 'good afternoon', event: 'conference meeting')))
try {
    println(member.sendAndWait(new Greeting(message: 'good evening', event: 'conference meeting')))
} catch (ex) {
    assert ex instanceof IllegalStateException
    println "sender got exception: $ex"
}

member.join()

