/**
 * Groovy category is another way of enhancing a group of objects by class type.
 * A Category allows you to save the behavior off in a separate class file
 *
 * Because categories are nothing more than classes with static methods, they
 * can be defined in Java code. As a matter of fact, you can reuse existing Java
 * classes as categories for their static defined methods as category methods.
 * For example, many apache commons util classes can be used as categories.
 *
 * Category is similar to Module in Ruby.
 */


// a category class has to define methods in static scope
class StringHelper {
    static void shout(String self) {
        println self.toUpperCase()
    }

    static void quiet(String self) {
        println "shhhhh..., don't tell them I am $self"
    }

    // now with some explicit arguments
    // Note the second argument in definition, the first arg disappears on method call
    static void tellMeMore(String self, args = []) {
        println "called with args: $args"
    }
}


String str = 'abc'
use(StringHelper) {
    str.shout()

    str.quiet()

    str.tellMeMore()
    str.tellMeMore(this)
}




