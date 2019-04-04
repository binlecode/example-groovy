

List dataList = (1..20).toList()

int binarySearch(List orderedList, int target, int low, int high) {
    println "low: $low, high: $high"
    int mid 
    if (low > high) {
        mid = -1
    } else {
        mid = (low + high)/2
        if (target < orderedList[mid]) {
            mid = binarySearch(orderedList, target, low, mid-1)
        } else if (target > orderedList[mid]) {
            mid = binarySearch(orderedList, target, mid+1, high)
        } else {
            // there is a match! mid is the target index
            println "found match, with index: $mid"
        }
    }
    return mid
}

binarySearch(dataList, 7, 0, 19)

println "now the non recursive way"

int binarySearchNonRecursive(List lst, int target) {
    if (!lst) return -1
    
    int low = 0
    int high = lst.size() - 1
    int mid
    while (low <= high) {
        println "low: $low, high: $high"
        mid = (low + high) / 2
        if (target < lst[mid]) {
            high = mid - 1
        } else if (target > lst[mid]) {
            low = mid + 1
        } else {
            println "found match, with index: $mid"
            return mid
        }
    }
    return -1

}

binarySearchNonRecursive(dataList, 7)