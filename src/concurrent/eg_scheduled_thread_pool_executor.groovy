package concurrent

import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.BlockingQueue
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

/**
 * Generic wrapper class for work item
 * @param <T> type of work item
 */
class WorkUnit<T> {
    T work

    // can add more work unit metadata in this class instead of the enclosing work T class

    WorkUnit(T wrk) {
        this.work = wrk
    }

    T getWork() {
        return work
    }
}

/**
 * Using groovy closure for Runnable / Callable simplifies async coding.
 */
class DemoScheduledThreadPoolExecutor {

    ScheduledExecutorService scheduledExecutorService
    BlockingQueue<WorkUnit<String>> workQueue

    DemoScheduledThreadPoolExecutor(int poolSize = 2, int capacity = 100) {
        scheduledExecutorService = Executors.newScheduledThreadPool(poolSize)
        workQueue = new LinkedBlockingQueue<>(capacity)

        10.times {
            workQueue.offer(new WorkUnit<String>("testing message $it"))
        }
    }

    // use groovy convenience of closure for Runnable
    final Runnable worker = { ->
        println "try polling a work unit"
        String msg = workQueue.poll()?.getWork()  // poll returns null when queue is empty
        if (msg) {
            println "message received: $msg"
        }
    } as Runnable

    ScheduledFuture startWorkflow() {
        ScheduledFuture handler = scheduledExecutorService.scheduleAtFixedRate(worker, 500, 500, TimeUnit.MILLISECONDS)
        return handler
    }

    def cancelWorkflow(ScheduledFuture handler) {
        // wrap a delayed cancellation as a Runnable and schedule it
        scheduledExecutorService.schedule({
            handler.cancel(true)
        } as Runnable, 100, TimeUnit.MILLISECONDS)
    }

}


def demo = new DemoScheduledThreadPoolExecutor()

def h = demo.startWorkflow()

Thread.sleep(10000)  // let run for 10s

// refill queue
20.times {
    def r =demo.workQueue.offer(new WorkUnit<String>("testing message refill-$it"))
    println "refill $it => $r"
}

Thread.sleep(3000)  // let run for another period


println "time is up, cancelling workflow"
demo.cancelWorkflow(h)

//demo.ses.awaitTermination(100, TimeUnit.MILLISECONDS)

println "shutting down"
demo.scheduledExecutorService.shutdown()



