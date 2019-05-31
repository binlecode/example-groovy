import groovyx.gpars.GParsPool
@Grab(group='org.codehaus.gpars', module='gpars', version='1.2.1')


import groovyx.gpars.actor.Actors
import groovyx.gpars.agent.Agent

import java.util.concurrent.TimeUnit

/**
 * Agent class is a special-purpose, thread-safe, non-blocking implementation that wraps a reference to
 * mutable state held inside a single field, and accepts code (closures or commands) as messages, which
 * can be sent to the Agent just like to any other actor using the '<<' operator, the send() methods or
 * the implicit call() method.
 * <p>
 * The closure sent to the agent is invoked against the internal mutable field and can make changes to it.
 * The closure is guaranteed to be run without intervention from other threads.
 * <p>
 * Note that the closure is sent to agent as a message (to its actor nature), so it is like 'fire and forget'.
 * The caller thread can go off to do other things and come back later to check Agent.val or Agent.valAsync(closure).
 * - The {@code val} property of an Agent will wait until all preceding commands in the agent’s queue are consumed
 *   and then safely return the value of the Agent.
 * - The {@code valAsync()} method will do the same without blocking the caller.
 */

Agent messageWall = new Agent<String>('sender 0')

// define a listener invoked by value change
messageWall.addListener { oldVal, newVal -> println "LISTENNER: value changed from $oldVal to $newVal" }

// define a validator, from output we can see validation listener is invoked BEFORE value-change listener
// validation error is collected in Agent.errors property, and value change is blocked.
// however, the agent keeps working receiving future messages
messageWall.addValidator { oldVal, newVal ->
    if (newVal.split('\\s')[1] < oldVal.split('\\s')[1]) {
        def errMsg = "VALIDATOR: error! new ($newVal) is less than old ($oldVal)"
        println errMsg
        throw new IllegalArgumentException(errMsg)
    }
}

def actors = []
Random rdn = new Random()

// have multiple threads sending value-assigning message to the agent with random delay
Actors.actor {
    GParsPool.withPool(10) {
        (1..10).eachParallel { idx ->
            TimeUnit.MILLISECONDS.sleep(rdn.nextInt(100))
            messageWall << "sender $idx"
        }
    }
}

// we define an observer in a separate thread with randomly delayed checking time
def observer = Actors.actor {
    loop(10) {
        TimeUnit.MILLISECONDS.sleep(rdn.nextInt(100))
        println "OBSERVER: value = ${messageWall.instantVal}"
    }
}

//println messageWall.val

observer.join()
actors*.join()

// print error message of collected exceptions from agent
if (messageWall.hasErrors()) {
    messageWall.errors.each {
        println it
    }
}


//todo: agent by default use a global daemon thread pool, can use comtom pool


