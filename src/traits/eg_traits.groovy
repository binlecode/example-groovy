trait Colorful {
    static def hasColor() {
        println "i am not just black-white: ${this}"
    }
}

trait IsRed extends Colorful {
    def showColor() {
        println "i am red: ${this}"
    }
}

// define a subtrait
class Box implements Colorful {
}

Box.hasColor()

def myBox = new Box() as IsRed  // runtime trait implementation
myBox.showColor()   // this prints a proxied class instance, not the original instance


class RedBox implements IsRed {

}

RedBox.hasColor()
new RedBox().showColor()


trait Container {
    Map store = [:]
    private def internalLabel = 'container'
    public static def classLabel = 'Container'

    static def feature() {
        println "i am container: ${this}"
    }

    def put(k, v) {
        store.put(k, v)
    }

    def showStore() {
        println store
    }

    def showInternallabel() {
        println internalLabel
    }
}


class MyBox implements Colorful, Container {
}

MyBox.feature()

def mb = new MyBox()
mb.put('toy', 'toy gun')
mb.showStore()

println mb.store
mb.showInternallabel()
// this will error out when access trait's private properties
try {
    println mb.internalLabel
} catch (Exception ex) {
    println "caught ex: $ex"
}
// traits static properties are shadowed from implementing class
try {
    println MyBox.classLabel
} catch (Exception ex) {
    println "caught ex: $ex"
}
println MyBox.Container__classLabel // translated static property name from class
println mb.Container__internalLabel // or access from instance


