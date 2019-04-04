



class StringSupport {
    // Groovy categories will require the injection method to be static and take at least one parameter
    static lowerCase(String self) {  // restricts input to be String only
        self?.toLowerCase()
    }

    static showOff(self) {  // this input is not restricted, aka, input is Object type
        return "show off: $self"
    }

}

// available in use context
use(StringSupport) {

    println 'Abc ABC'.lowerCase()

    println new Date().showOff()  // caller can be any object as showOff() takes Object type

}

// this ex is expected
try {
    println 'Abc'.lowerCase()
} catch (groovy.lang.MissingMethodException ex) {
    println 'expected ex: ' + ex.message
}

// a better way is to use annotation Category
@Category(String)   // this category is to apply for String type host object
class StringSupportAnnotated {

    def lowerCase() {
        this.toLowerCase()  // 'this' refers to host object
    }

    def showOff() {
        "show off: $this"
    }

}

use(StringSupportAnnotated) {

    println 'aBc'.lowerCase()
    println 'aBc ABC'.showOff()

}