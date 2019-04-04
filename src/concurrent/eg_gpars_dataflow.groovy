import groovyx.gpars.dataflow.DataflowQueue
import groovyx.gpars.dataflow.Promise

@Grab(group='org.codehaus.gpars', module='gpars', version='1.2.1')

import static groovyx.gpars.dataflow.Dataflow.task


def words = ['Groovy', 'fantastic', 'concurrency', 'fun', 'enjoy', 'safe', 'GPars', 'data', 'flow']

final def buffer = new DataflowQueue()


Promise tPromise = task {
    for (word in words) {
        buffer << word.toUpperCase()
        sleep 1000
        // throw new Exception('ex from within loop!!!')
    }
    return 123
}.then({ result ->      // handle result
    println "result: ${result}"
}, { ex ->        // handle exception
    println "error: $ex"
})

task {
    while (true) {
        println buffer.val
    }
}

println 'reached the last line of program'

println "tPromise return: ${tPromise.get()}"