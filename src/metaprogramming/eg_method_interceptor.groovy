

class Worker {
    void sayHello(target=null) { println('hello worker! ' + target) }
}

def worker = new Worker()
worker.metaClass.invokeMethod = { name, args -> 
    println "calling $name with args: $args"
    def res = delegate.metaClass.getMetaMethod( name, args ).invoke( delegate, args ) 
    println "after $name. res = $res"
    res 
}

worker.sayHello()