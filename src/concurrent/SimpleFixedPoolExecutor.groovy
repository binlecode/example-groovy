package concurrent

import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 *
 * Provides a wrapper of java ExecutorService with a fixed blocking queue thread pool.
 * This executor is constructed with custom pool size, queue size and rejection policy.
 * It does not have custom ThreadFactory thus no in-thread exception handling, in other words, the exception
 * within the thread may not be passed out to the caller thread.
 *
 * It is callable/runnable object's responsibility to catch exceptions, which will otherwise terminate the running
 * task silently.
 *
 * Another to catch in-thread exception is to subclass {@link ThreadPoolExecutor} to override
 * {@link ThreadPoolExecutor#afterExecute(java.lang.Runnable, java.lang.Throwable)} method for generic exception
 * handling.
 *
 * Usage:
 *
 * Create a grails service bean extending this class, disable transactional.
 * Override config key setting methods to provide use-case specific key settings and rejection policy.
 * Note the capacity of the concurrent handling is (number of threads + queue size)
 *
 */
class SimpleFixedPoolExecutor {

    /**
     * This executor is constructed with custom pool size, queue size and rejection policy.
     * It does NOT have custom ThreadFactory thus no in-thread exception handling, in other words, the exception
     * within the thread may NOT be passed out to the caller thread.
     * <p>
     * This executor uses blocking queue to bound the number of submitted queue-able tasks.
     * There is a trade off between queue size and pool size. Use large queue size and moderate pool size
     * to save resources, but resulting in low throughput.
     * <p>
     * This executor supports custom CallerRunsPolicy to force caller thread to run the job too to 'block' incoming
     * requests or use AbortPolicy to throw RejectedExecutionException to prevent queuing up at controller side.
     * <p>
     * @param options includes pool properties such as
     * - threadPoolShutDownAwaitingTime
     * - threadPoolMaxThreads
     * - threadPoolQueueSize
     * - threadPoolRejectionPolicy: 'abort' or 'callerRuns', default to 'abort'
     */
    SimpleFixedPoolExecutor(Map<String, Object> options) {

        if (options.threadPoolShutDownAwaitingTime instanceof Integer) {
            this.threadPoolShutDownAwaitingTime = options.threadPoolShutDownAwaitingTime
        } else {
            this.threadPoolShutDownAwaitingTime = 2
        }

        if (options.threadPoolMaxThreads instanceof Integer) {
            this.threadPoolMaxThreads = options.threadPoolMaxThreads
        } else {
            this.threadPoolQueueSize = Runtime.getRuntime().availableProcessors() + 1
        }

        if (options.threadPoolQueueSize instanceof Integer) {
            this.threadPoolQueueSize = options.threadPoolQueueSize
        } else {
            this.threadPoolQueueSize = this.threadPoolMaxThreads
        }

        if (['abort', 'callerRuns'].contains(options.threadPoolRejectionPolicy)) {
            this.threadPoolRejectionPolicy = options.threadPoolRejectionPolicy
        } else {
            this.threadPoolRejectionPolicy = 'abort'
        }

        this.executor = new ThreadPoolExecutor(
                this.threadPoolMaxThreads, // core thread pool size
                this.threadPoolMaxThreads, // maximum thread pool size
                1, // time to wait before resizing pool
                TimeUnit.MINUTES,
                new ArrayBlockingQueue<Runnable>(this.threadPoolQueueSize, true),

                (this.threadPoolRejectionPolicy == 'abort' ? new ThreadPoolExecutor.AbortPolicy() : new ThreadPoolExecutor.CallerRunsPolicy())
        )
    }

    /**
     * pool shut down awaiting time period, in seconds, default to 2
     */
    protected final Integer threadPoolShutDownAwaitingTime

    /**
     * threadPoolQueueSize value of a reasonable integer, default to threadPoolMaxThreads
     */
    protected final Integer threadPoolQueueSize

    /**
     * threadPoolMaxThreads value of a reasonable interger, default to (#cpus + 1)
     */
    protected final Integer threadPoolMaxThreads

    /**
     * threadPoolRejectionPolicy string of value 'abort' or 'callerRuns', default to 'abort'
     */
    protected final String threadPoolRejectionPolicy   // 'abort' or 'callerRuns'

    @Override
    String toString() {
         "SimpleFixedPoolExecutor " + this + "\n"
                 + "threadPoolMaxThreads = ${this.threadPoolMaxThreads}"
                 + "threadPoolQueueSize = ${this.threadPoolQueueSize}"
                 + "threadPoolRejectionPolicy = ${this.threadPoolRejectionPolicy}"
                 + "threadPoolShutDownAwaitingTime = ${this.threadPoolShutDownAwaitingTime}"
                 + "threadpool executor = ${this.executor}"
    }

    @Delegate
    ExecutorService executor  // injected with a custom thread pool

    /**
     * A task queued with execute() that generates some Throwable will cause the UncaughtExceptionHandler
     * for the Thread running the task to be invoked.
     * The default UncaughtExceptionHandler, which typically prints the Throwable stack trace to System.err,
     * will be invoked if no custom handler has been installed.
     */
    public void execute(Runnable command) {
        executor.execute(command)
    }

    /**
     * A Throwable generated by a task queued with submit() will bind the Throwable to the Future that was
     * produced from the call to submit().
     * Calling get() on that Future will throw an ExecutionException with the original Throwable as its
     * cause (accessible by calling getCause() on the ExecutionException).
     */
    public <T> Future<T> submit(Callable<T> task) {
        executor.submit(task)
    }

    /**
     * Submits a Runnable task for execution and returns a Future representing that task.
     * The Future's get method will return null upon successful completion.
     */
    public Future<?> submit(Runnable task) {
        executor.submit(task)
    }

    /**
     * A type specific submit version, the result type is given as parameter
     * @param task
     * @param result the type of result to be returned by future
     */
    public <T> Future<T> submit(Runnable task, T result) {
        executor.submit(task, result)
    }

    /**
     * recommend call this method before destroying the executor
     */
    void destroy() throws Exception {
        executor.shutdown()  // gracefully shut down thread pool until last ongoing task is done
        if (!executor.awaitTermination(this.threadPoolShutDownAwaitingTime, TimeUnit.SECONDS)) {
            // executor service did not shutdown in given seconds. Forcing shutdown of any scheduled tasks"
            executor.shutdownNow()
        }
    }

}
