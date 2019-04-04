


class MySimpleCloneable implements Cloneable {
	String name

	MySimpleCloneable(String name) {
		this.name = name
	}

	void greet(String[] args) {
		println "hello MySimpleCloneable instance: $name"
	}
	/** clone() method is actually implemented as a protected 
	 * method in Object class. So you don't have to override it
	 * if it only contains primative fields, or complex Cloneable 
	 * fields.
	 */
	/*
	@Override
	MyCloneable clone() {
		super.clone()
	}
	*/
}

MySimpleCloneable m = new MySimpleCloneable('my cloneable instance')
def mc = m.clone()
assert m.name == mc.name

// a deep clone example

class MyCloneable implements Cloneable {
	String name
	MySimpleCloneable mySimpleCloneable

	MyCloneable(String name, MySimpleCloneable mySimpleCloneable) {
		this.name = name
		this.mySimpleCloneable = mySimpleCloneable
	}

	/** In this case we must override clone()
	 * since we have MySimpleCloneable class as field
	 */
	@Override
	MyCloneable clone() {
		MyCloneable clone = (MyCloneable) super.clone()  // first get a shallow copy version
		clone.mySimpleCloneable = clone.mySimpleCloneable.clone() // replace shallow field with deep cloned field
		return clone
	}

}

def myCloneable = new MyCloneable('my cloneable instance', new MySimpleCloneable('my simpleCloneable instance'))
def cloneOfMyCloneable = myCloneable.clone()
assert cloneOfMyCloneable.name == myCloneable.name
assert cloneOfMyCloneable.mySimpleCloneable != myCloneable.mySimpleCloneable
assert cloneOfMyCloneable.mySimpleCloneable.name == myCloneable.mySimpleCloneable.name


// a deeper clone example

class MyComplexCloneable implements Cloneable {
	String name
	List<MyCloneable> cloneables = []

	/** We must override clone() method to take care of container fields
	 */
	@Override
	MyComplexCloneable clone() {
		MyComplexCloneable clone = (MyComplexCloneable) super.clone()
	    def cloneOfMyCloneables = new ArrayList<MyCloneable>()
	    clone.cloneables.each {
	    	cloneOfMyCloneables.add it.clone()
	    }
	    clone.cloneables = cloneOfMyCloneables
	    return clone
	}
}


def complexCloneable = new MyComplexCloneable()
complexCloneable.name = 'my complex cloneable instance'
complexCloneable.cloneables = [
	new MyCloneable('my cloneable instance 1', new MySimpleCloneable('my simpleCloneable instance 1')),
	new MyCloneable('my cloneable instance 2', new MySimpleCloneable('my simpleCloneable instance 2')),
	new MyCloneable('my cloneable instance 3', new MySimpleCloneable('my simpleCloneable instance 3')),
]
def cloneOfComplexCloneable = complexCloneable.clone()
assert cloneOfComplexCloneable.name == complexCloneable.name
assert cloneOfComplexCloneable.cloneables.size() == complexCloneable.cloneables.size()
assert cloneOfComplexCloneable.cloneables != complexCloneable.cloneables
cloneOfComplexCloneable.cloneables.eachWithIndex { c, idx ->
	def cloneable = complexCloneable.cloneables[idx]
	assert c != cloneable
	assert c.name == cloneable.name
}


