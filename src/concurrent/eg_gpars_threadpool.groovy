@Grab(group = 'org.codehaus.gpars', module = 'gpars', version = '1.2.1')
import groovyx.gpars.GParsExecutorsPool

def longRunningMethod(name) {
    println "hello $name, running ..."
    Thread.sleep(3000)
}

// customize thread pool size
GParsExecutorsPool.withPool(2) {
    def longRunningMethodWrapper = { arg ->
        longRunningMethod(arg)
    }

    ['a', 'b', 'c', 'd', 'e'].each {

        longRunningMethodWrapper.callAsync(it)
    }
}



