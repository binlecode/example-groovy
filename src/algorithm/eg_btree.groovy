
// create a binary tree with an array list

class Node {
    int value
    MyNode leftChild
    MyNode rightChild
    MyNode parent
    
    public Node(int value, MyNode left=null, MyNode right=null, MyNode parent=null) {
        this.value = value
        if (left) this.leftChild = left
        if (right) this.rightChild = right
        if (parent) this.parent = parent
    }
    
    @Override
    public String toString() {
        return "Value: $value, parent -> ${parent?.value}, left child -> ${leftChild?.value}, right child -> ${rightChild?.value}"
    }
}

// a rumtime parent node queue for appending child nodes
Queue<MyNode> pNodeQueue = new LinkedList<MyNode>()

MyNode node
MyNode pNode

def inputArr = (1..10).toList()

MyNode[] btree = new MyNode[10]

inputArr.eachWithIndex { nv, idx -> 
    println "insert new node with value: $nv"
    
    node = new MyNode(nv)
    btree[idx] = node
    
    
    pNode = pNodeQueue.peek()
    if (pNode) {
        if (pNode.leftChild == null) {
            pNode.leftChild = node
        } else {
            pNode.rightChild = node
            pNodeQueue.remove()  // remove this parent node from queue as both its left are right are filled
        } 
    }
    pNodeQueue.add(node)  // add new node into runtime queue as potential parent for future new nodes
    
    println "current queue:"
    pNodeQueue.each { println it }  
}

println "created btree:"
btree.each { println it }


// pre-order tree walk
def preOrderWalk(MyNode node) {
    if (node) {
        println "touch node: $node"
        preOrderWalk(node.leftChild)
        preOrderWalk(node.rightChild)
    }
}
println "Pre-order tree walk trace:"
preOrderWalk(btree[0])

// in-order tree walk
def inOrderWalk(MyNode node) {
    if (node) {
        inOrderWalk(node.leftChild)
        println "touch node: $node"
        inOrderWalk(node.rightChild)
    }
}
println "In-order tree walk trace:"
inOrderWalk(btree[0])

// post-order tree walk
def postOrderWalk(MyNode node) {
    if (node) {
        postOrderWalk(node.leftChild)
        postOrderWalk(node.rightChild)
        println "touch node: $node"
    }
}
println "Post-order tree walk trace:"
postOrderWalk(btree[0])

// level-order tree walk
// this is breath-first type, so use queue (FIFO) instead of recursive stack
def levelOrderWalk(MyNode node) {
    if (!node) return
    Queue<MyNode> nQueue = new LinkedList<MyNode>()
    nQueue.add(node)
    
    while (nQueue.size() > 0) {
        node = nQueue.peek()
        println "touch node: $node"
        
        if (node.leftChild) nQueue.add(node.leftChild)
        if (node.rightChild) nQueue.add(node.rightChild)
        // after both left and right are added to queue, the current node should be removed from queue to avoid future walk 
        nQueue.remove()  
    }
}
println "Level-order tree walk trace:"
levelOrderWalk(btree[0])



