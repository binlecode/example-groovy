/**
 * enum types let you add arbitrary methods and fields and implement arbitrary interfaces.
 * They implement Comparable and Serializable.
 *
 * To write a rich enum type, associate data with
 * enum constants, declare instance fields and write a constructor that takes the
 * data and stores it in the fields.
 * Enums are by their nature immutable, so all fields
 * should be final.
 */
enum Hobby { 
	LEGO(10, 3),  // (cost, interest)
	RC_PLANE(100, 5),
	TENNIS(50, 10)

    public final int cost
    public final int interest

    public Hobby(cost, interest) {
        this.cost = cost
        this.interest = interest
    }

    // define methods for projection or calculation
	def getPopularity() {
        return interest / Math.max(cost, 1f)
	}

    // define advanced methods for Instance adaptive logic
    def declear() {
        switch (this) {
            case LEGO:
                println "this is best for kids having money more than $cost"
                break
            case RC_PLANE:
                println "this is ideal for teenagers to kill time in the park"
                break
            case TENNIS:
                println "this is serious deal for grown-ups with deep pocket more than $cost"
                break
            default:
                break
        }
    }
}

println Hobby.LEGO.cost
println Hobby.TENNIS.interest
println Hobby.LEGO.popularity
Hobby.LEGO.declear()
println Hobby.RC_PLANE.popularity
Hobby.RC_PLANE.declear()
println Hobby.TENNIS.popularity
Hobby.TENNIS.declear()

/**
 * Here we are defining constant specific method, which overrides the common abstract one.
 * A disadvantage of constant-specific method implementations is that they
 * make it harder to share code among enum constants.
 * With a switch statement as shown in above snippet, it’s easy to
 * do this by applying multiple case labels to each of them. But the hidden danger is by
 * using switching logic in a common code block, it is easy to forget adding new case when
 * a new constant is added to the enum group. So, in general switch-case is not a best way
 * of and make a decision with
 * caution.
 */
enum MyService {
    FOR_ADD {
        @Override
        int operate(int x, int y) {
            x + y
        }
        // one common case is to override toString() for each enum constant
        @Override
        String toString() {
            "this add operation"
        }
    },
    FOR_MINUS {
        @Override
        int operate(int x, int y) {
            x - y
        }
        @Override
        String toString() {
            "this is minus operation"
        }
    },
    FOR_DEFAULT {
    }

//    abstract int operate(int x, int y)
    int operate(int x, int y) {  // doesn't have to be abstract to carry default logic
        println "please implement me in each constant override for input $x and $y"
        return 0
    }

    String toString() {
        "this is default toString method"
    }
}

println MyService.FOR_ADD.operate(1, 3)
println MyService.FOR_MINUS.operate(3, 1)
println MyService.FOR_DEFAULT.operate(3, 1)

println MyService.FOR_ADD
println MyService.FOR_MINUS

// Enum types have an automatically generated valueOf(String) method that
// translates a constant’s name into the constant itself. It is mostly used to
// get the instance by it name string
println MyService.valueOf('FOR_ADD').class.name
println MyService.valueOf('FOR_MINUS').toString()