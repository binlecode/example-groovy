

class HelloMixin {
    // this asseumes target class instance field 'tag'
    def greet() {
        println "this is tag $tag"
    }

    def hello() {
        println "hello $tag"
    }

    static def staticHello() {
        println "static hello"
    }
       
}

// static mixing is done at class level, can mixin instance method
class MyClass {
    static {
        MyClass.mixin HelloMixin
    }

    def tag

    def greet() {
        println 'this is my class'
    }
}


def mc = new MyClass()
mc.tag = 'mc tag'
mc.greet()   // this will print 'hello mc tag'
mc.hello()


// we can also apply mixing at runtime on instance metaClass object
// Note: mixing is done at instance level, and ONLY applies to that instance
class MyTgtClass {
    def tag

    def greet() {
        println 'this is my target class'
    }
}

def mtc = new MyTgtClass()
mtc.tag = '-= mtc tag =-'
mtc.greet()

mtc.metaClass.mixin HelloMixin

mtc.hello()
mtc.staticHello()

// this will blow on another instance since its metaClass doesn't have mixin
def anotherMtc = new MyTgtClass()
anotherMtc.tag = '== another mtc tag =='
try {
    anotherMtc.hello()
} catch (Exception e) {
    assert e instanceof groovy.lang.MissingMethodException
    assert e.message.startsWith('No signature of method: MyTgtClass.hello() is applicable for argument types: () values: []')
}





