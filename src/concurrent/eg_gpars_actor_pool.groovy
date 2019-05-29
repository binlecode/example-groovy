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
        react {
            println "with thread [${Thread.currentThread().name}] actor No. ${seqNumber} got msg: $it"
        }
    }
}

def pg1 = new DefaultPGroup(3)
def pg2 = new DefaultPGroup(8)

def actorList1 = (1..10).collect { new nActor(pg1, it) }*.start()
def actorList2 = (1..10).collect { new nActor(pg2, it) }*.start()

actorList1*.send('hello team 1')

TimeUnit.MILLISECONDS.sleep(500)

actorList2*.send('hello team 2')

actorList1*.join()
actorList2*.join()

pg1.shutdown()
pg2.shutdown()



