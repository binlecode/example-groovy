/**
 * Groovy metaClass object is attached to each groovy and java object.
 * It is similar to singleton object in ruby.
 */



// metaClass of class object

String.metaClass.shout = {
    // here the delegate is the outer class the closure is constructed,
    // which in this case is metaClass's owner: String class object
    println delegate.toUpperCase()
}

'abc'.shout()

// metaClass of instance object
// both shout and myShout only belong to str instance

def str = 'abcdefg'
str.metaClass.shout = {
    assert String == delegate.class   // delegate class is String
    println "delegate: $delegate"
    assert 'eg_meta_class' == "${owner.class.name}"  // owner is the script name
    println "this is instance: ${delegate.toUpperCase()}"  // this 'overrides' the class level closure
}
str.metaClass.myShout = {
    println "delegate: $delegate"
    delegate.shout()
}

str.shout()
str.myShout()

// let's prove that instance metaClass extended closures are only visible to its owning object
def str2 = 'xyz'
str2.shout()  // falls back to String.metaClass's shout closure
try {
    str2.myShout() // throws MissingMethodException exception
} catch (Exception e) {
    assert e instanceof groovy.lang.MissingMethodException
    assert e.message.startsWith('No signature of method: java.lang.String.myShout() is applicable for argument types: () values: []')
}


// now let's see static methods on meta class

String.metaClass.static.staticShout = {
    println "delegate: $delegate"
    println "static shout: ${delegate.toString().toUpperCase()}"
    delegate
}
// delegate changes when the caller changes!!!
assert java.lang.String == String.staticShout()
assert 'foo' == 'foo'.staticShout()

// things become more interesting when dealing with instance metaClass

def bar = 'bar'
bar.metaClass.static.myStaticShout = {
    println "delegate: $delegate"
    println "my static shout: ${delegate.toUpperCase()}"
    delegate
}
assert bar.is(bar.myStaticShout())   // note that delegate is still bar !!!
try {
    String.myStaticShout()   // bar's instance level metaClass's static closure is not visible to String class
} catch (e) {
    assert e instanceof groovy.lang.MissingMethodException
    assert e.message.startsWith('No signature of method: static java.lang.String.myStaticShout() is applicable for argument types: () values: []')
}


// let's see nested closure definition side effects
String.metaClass {
    showDelegate = {
        println "delegate: $delegate"  // owner is no longer the script, but the outer (anonymous) closure
    }
    showOwner = {
        println "owner: ${owner.class.name}"
    }
}


def hello = 'hello'
hello.showDelegate()
hello.showOwner()

hello.metaClass {
    showDelegate = {
        println "nested delegate: $delegate"
    }
    showOwner = {
        println "nested owner: ${owner.class.name}"
    }
}
hello.showDelegate()
hello.showOwner()