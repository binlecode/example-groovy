

class FibNumber {
    
    static def fib(int n) {
        if (n == 0) return 0
        if (n == 1) return 1
        
        return  fib(n-1) + fib(n-2)
    }
    
    static def fibNonRecursive(int n) {
        if (n == 0) return 0
        if (n == 1) return 1
        
        def backOne = 1    // serve as parameter stack variable
        def backTwo = 0    // serve as parameter stack variable
        def current        // serve as return stack veriable
        
        for (def i = 2; i <= n; i++) {
            current = backOne + backTwo
            backTwo = backOne
            backOne = current
        }
        return current
    }
}

12.times { println FibNumber.fib(it) }

12.times { println FibNumber.fibNonRecursive(it) }


