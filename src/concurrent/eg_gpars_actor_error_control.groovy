@Grab(group='org.codehaus.gpars', module='gpars', version='1.2.1')

import groovyx.gpars.actor.Actors

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
// this forces the main thread to wait until the actor thread is completed
act.join()

/**
 * Now we have above mechanism to make the actor stop nicely.
 * But to keep actor continue working with error, we need Supervising that supports:
 * - Get notified of changes on other actors
 * - Take control over them (stopping, restarting…etc)
 */

