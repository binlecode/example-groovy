@Grab(group='org.codehaus.gpars', module='gpars', version='1.2.1')

import groovyx.gpars.actor.Actors
import groovyx.gpars.dataflow.Promise
import jdk.nashorn.internal.ir.annotations.Immutable

import java.util.concurrent.TimeUnit


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
        terminate()
    }
}
oneTimeReceiver.send('hello once')
TimeUnit.MILLISECONDS.sleep(500)  // after this delay the actor is already terminated
try {
    oneTimeReceiver.send('hello twice')  // nothing happens
} catch (ex) {
    println "caught exception: ${ex.message}"
    assert ex instanceof IllegalStateException
    assert ex.message == 'The actor cannot accept messages at this point.'
}

// make message object immutable as much as you can (if not always)
@Immutable
class Greeting {
    String message
    String event
}

def member = Actors.actor {
    loop {
        react { greeting ->
            println "${greeting.message} in this ${greeting.event}"
//            sender.send "nice to meet you too"  // alternative way to send replies
            reply 'pleasure'
        }
    }
}

println(member.sendAndWait(new Greeting(message: 'good evening', event: 'conference meeting')))

