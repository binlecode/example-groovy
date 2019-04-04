class Duck {
    def quack() {
        println "Quack, I am a ${this.getClass()}"  // runtime type resolving
    }
}

class Dog {
    def quack() {
        println "Wof, I am a ${this.getClass()}"
    }
}

def quackable = [new Duck(), new Dog()]
quackable.each {
    it.quack()
}