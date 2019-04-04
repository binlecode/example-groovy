
class Hanoi {

    static def hanoi(int n, a, b, c) {
        if (n == 1) {
            println "Move 1 plate from $a to $c"
            return
        }
        
        // move n-1 plates from a to b
        hanoi(n-1, a, c, b)
        
        // move last one left on a to c
        println "Move 1 plate from $a to $c"
        
        // move n-1 plates from b to c
        hanoi(n-1, b, a, c)
    }

}

Hanoi.hanoi(4, 'A', 'B', 'C')