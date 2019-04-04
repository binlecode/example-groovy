package concurrent

import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

// singleThreadExecutor is preferred way of running a simple async work over Thread class
def stes = Executors.newSingleThreadExecutor()

// use groovy closure for Runnable
def worker = { ->
    def threadName = Thread.currentThread().getName()
    println "${threadName}: task running"
    3.times {
        TimeUnit.MILLISECONDS.sleep(500)
        println "${threadName}: task still running ..."
    }
    println "${threadName}: task done"
} as Runnable


// submit a Runnable which doesn't return
stes.submit(worker)

println "${Thread.currentThread().name}: is main thread"

// submit a Callable which returns Future result
Future<String> fut = stes.submit({ ->
    return "${Thread.currentThread().name}: this message comes from future"
} as Callable<String>)

try {
    def result = fut.get(1000, TimeUnit.MILLISECONDS)  // get is a blocking call, can put a time limit on it
    println "${Thread.currentThread().name}: future return $result"
} catch (TimeoutException ex) {
    println "${Thread.currentThread().name}: time is up, no return yet"
} catch (InterruptedException ex) {

}

// recommended pattern for shutting down a executor
try {
    stes.shutdown()
    stes.awaitTermination(5, TimeUnit.SECONDS)
} catch (InterruptedException ex) {
    println "${Thread.currentThread().name}: system interrupted"
} finally {
    stes.shutdownNow()  // force shutdown without grace period
}

// use fixed pool with nThread = 1 is equivalent to singleThreadExecutor
def ftpes = Executors.newFixedThreadPool(1)

ftpes.submit(worker)

try {
    ftpes.shutdown()
    ftpes.awaitTermination(3, TimeUnit.SECONDS)
} catch (InterruptedException ex) {

} finally {
    ftpes.shutdownNow()
}



