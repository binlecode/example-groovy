@Grab(group='org.codehaus.gpars', module='gpars', version='1.2.1')

import groovyx.gpars.group.DefaultPGroup
import groovyx.gpars.group.PGroup
import groovyx.gpars.actor.DefaultActor

import java.util.concurrent.TimeUnit


/**
 * Actors can be organized into groups and, as a default, there’s always an application-wide pooled
 * actor group available. It is used by the {@code Actors} abstract factory to create actors.
 * <p>
 * Custom groups can be used to create actors without consuming application global pool. Actors from
 * the same group share the same thread pool.
 */
def myPg = new DefaultPGroup(1) // build a pool group with pool size = 1
def myAct = myPg.actor {
    react {
        println "got $it"
    }
}

myAct << 'one'
myAct << 'two'  // got ignored

myAct.join()

// always shutdown custom pooled actor groups, once they are no longer needed, to preserve system resources
myPg.shutdown()


/**
 * It’s recommended to group a massive number of actors into multiple PGroups with fixed size thread pools.
 */
class nActor extends DefaultActor {
    private seqNumber

    public nActor(PGroup pg, int seqNumber) {
        this.parallelGroup = pg
        this.seqNumber = seqNumber
    }

    void act() {
        loop {
            react {
                println "with thread [${Thread.currentThread().name}] actor No. ${seqNumber} got msg: $it"
            }
        }
    }
}

def pg1 = new DefaultPGroup(3)
def pg2 = new DefaultPGroup(8)

def actorList1 = (1..10).collect { new nActor(pg1, it) }*.start()
// by default actors are created in unfair mode, they try to retain the thread when their own message queue is not empty
// In this example we make team 1 of unfair actors, and team 2 with fair actors
def actorList2 = (1..10).collect {
    def a = new nActor(pg2, it)
    a.makeFair()  // make actor working in fair mode: give back thread when each message is handled
    a
}*.start()

// purposely send two messages in a row to each actor to create a message queue for each actor
actorList1.each {
    it << 'hello team 1'
    it << 'hello again team 1'
}

TimeUnit.MILLISECONDS.sleep(1000)

actorList2.each {
    it << 'hello team 2'
    it << 'hello again team 2'
}

/*
The output from team 1 is like:

with thread [Actor Thread 3] actor No. 3 got msg: hello team 1
with thread [Actor Thread 3] actor No. 3 got msg: hello again team 1
with thread [Actor Thread 3] actor No. 4 got msg: hello team 1
with thread [Actor Thread 3] actor No. 4 got msg: hello again team 1
with thread [Actor Thread 3] actor No. 5 got msg: hello team 1
with thread [Actor Thread 3] actor No. 5 got msg: hello again team 1
with thread [Actor Thread 3] actor No. 6 got msg: hello team 1
with thread [Actor Thread 3] actor No. 6 got msg: hello again team 1
...

While the output from team 2 is like:

with thread [Actor Thread 7] actor No. 1 got msg: hello team 2
with thread [Actor Thread 7] actor No. 2 got msg: hello team 2
with thread [Actor Thread 9] actor No. 4 got msg: hello team 2
...
with thread [Actor Thread 7] actor No. 1 got msg: hello again team 2
with thread [Actor Thread 9] actor No. 10 got msg: hello team 2
with thread [Actor Thread 11] actor No. 2 got msg: hello again team 2
...

We can see team 1 each actor is trying to hold the thread until its message queue is empty,
while team 2 each actor is giving back thread after each message is handled.
 */

actorList1*.join()
actorList2*.join()

pg1.shutdown()
pg2.shutdown()
