@Grab(group='org.codehaus.gpars', module='gpars', version='1.2.1')

import groovyx.gpars.GParsPool

GParsPool.withPool {
    (1..100).findAllParallel {
        it > 40
    }.collectParallel { it * 2 }
    
    //(1..100).filter { it < 20 }
}
