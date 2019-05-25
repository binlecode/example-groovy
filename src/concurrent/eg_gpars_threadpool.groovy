@Grab(group = 'org.codehaus.gpars', module = 'gpars', version = '1.2.1')
import groovyx.gpars.GParsExecutorsPool
import groovyx.gpars.GParsPool


// Quick and dirty: .withPool() provides a thread pool as an instance of ForkJoinPool
// The withPool method returns only after all the worker threads have finished their tasks
// and the pool has been destroyed, returning the resulting value of the associated block of code.
GParsPool.withPool {   //
    // gpars parallel array processing preserves source array's original order
    println((1..100).findAllParallel {
        it > 40
    }.collectParallel { it * 2 })   // gpars provides a lot of xxxParallel methods
}


/**
 * The difference between GParsPool and GParsExecutorsPool is, the former uses fork/join pool, while the
 * latter doesn't, instead it uses JDK standard executors service to create thread pool.
 * In general, GParsPool typically performs much better than GParsExecutorsPool does.
 */

def longRunningMethod(name) {
    println "hello $name, running ..."
    Thread.sleep(1000)
}

// customize pool size
GParsExecutorsPool.withPool(2) {
    def longRunningMethodWrapper = { arg ->
        longRunningMethod(arg)
    }

    ['a', 'b', 'c', 'd', 'e'].each {
        longRunningMethodWrapper.callAsync(it)
    }
}

// we can also import static withPool method
import static groovyx.gpars.GParsPool.withPool

def rnd = new Random(new Date().time)
withPool {
    def cnt = (1..100).collect {rnd.nextInt(99)}.countParallel { it > 50 }
    println "how many above 50: $cnt"
}

