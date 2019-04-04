@Grab(group = 'org.codehaus.gpars', module = 'gpars', version = '1.2.1')
import groovyx.gpars.GParsExecutorsPool

def longRuningMethod(name) {
    println "hello $name, running ..."
    Thread.sleep(3000)
}


    GParsExecutorsPool.withPool(2) {
        def longRunningMethodWrapper = { arg ->
            longRuningMethod(arg)
        }

        ['a', 'b', 'c', 'd', 'e'].each {

            longRunningMethodWrapper.callAsync(it)
        }
    }



