@Grab(group='org.codehaus.gpars', module='gpars', version='1.2.1')

import groovyx.gpars.GParsPool

import java.util.concurrent.atomic.AtomicInteger

/**
 * If an exception is thrown while processing any of the passed-in closures, the first exception is re-thrown
 * from the xxxParallel methods and the algorithm stops as soon as possible.
 * <p>
 * The exception handling mechanism of GParsPool builds on the one provided in the Fork/Join framework.
 * Since Fork/Join algorithms are by nature hierarchical, once any part of the algorithm fails, there’s usually
 * little benefit continuing the computation, since some branches of the algorithm will never return a result.
 * <p>
 * Note that GParsPool implementation does not give any guarantees about its behavior after a first unhandled
 * exception occurs, beyond stopping the algorithm and re-throwing the first detected exception to the caller.
 * It essentially hands over the error control to the caller thread.
 */
try {
    GParsPool.withPool {
        def rnd = new Random()
        (1..10).collect { rnd.nextInt(10) }.eachParallel {
            println "number: ${it}"
            if (it > 5) {
                // only the first exception is rethrown to the caller (.eachParallel method) thread
                throw new RuntimeException("error on > 5 number: $it")
            }
        }
    }
} catch (ex) {
    println "caught error: ${ex.message}"
}

/*
a typical running display is like:
number: 1
number: 7
number: 2
number: 7
number: 4
number: 4
number: 2
number: 9
number: 0
caught error: java.lang.RuntimeException: error on > 5 number: 7

It is clear that only the first exception (number 7) is thrown to caller (try-catch) thread.
 */

