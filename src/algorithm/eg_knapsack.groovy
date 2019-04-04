/** 
 * NP knapsack problem for an exact total weight match
 */
class Knapsack {
    
    static int weight(n) {
        return n*3 + 7
    }
    static items = [1, 2, 3, 4, 5]
    static inclItems = []
    
    static boolean knap(int s, int n) {
        def wn = weight(n)
        println "$n th item weight: $wn"
        println "trying $n items for $s"
        
        if (n <= 0) return false
        
        if (s == weight(n)) {
            this.inclItems << n
            return true  // success
        }
        
        if (s < weight(n)) {  // this item can not be in the solution set
            return knap(s, n - 1)  // remove item n
        } else {      
            this.inclItems << n  
            return knap(s - weight(n), n - 1)  // include item n and continue with rest n-1 items
        }
    }
}

def numOfItems = 5
println "items weight:"
(numOfItems).times {
    println Knapsack.weight(it+1)
}

(numOfItems).times {
    println Knapsack.knap(26, it+1)
    println Knapsack.inclItems
    Knapsack.inclItems.clear()
}



