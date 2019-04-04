

class Car {
    def horn(String msg) { println "horning - $msg" }
}

Car.metaClass.invokeMethod = { String name, args ->
    println 'before call'
    Car.metaClass.getMetaMethod(name).invoke(delegate, args)
    println 'after call'
}

myCar = new Car()
myCar.horn('go away!')


println myCar.metaClass.class.name



println Car.metaClass.class.name