
// create a ordered binary tree with an array list

class MyNode {
    int value
    MyNode leftChild
    MyNode rightChild
    
    public MyNode(int value, MyNode left=null, MyNode right=null) {
        this.value = value
        if (left) this.leftChild = left
        if (right) this.rightChild = right
    }
    
    public boolean isLeaf() {
        leftChild == null && rightChild == null
    }
    
    @Override
    public String toString() {
        return "Value: $value, left child -> ${leftChild?.value}, right child -> ${rightChild?.value}"
    }
}

class OrderedBTree {
    List<MyNode> btree
    
    public OrderedBTree() {
        btree = new LinkedList<MyNode>()
    }
    public OrderedBTree(List dataList) {
        btree = new LinkedList<MyNode>()
        if (dataList.size() > 0) {                
            for (int i = 0; i < dataList.size(); i++) {
                println "insert new node with value: ${dataList[i]}"
                insertNode(new MyNode(dataList[i]))
            } 
        }   
    }
    
    /**
     * Checks node by value before insert, skip insert if node found with matched value
     */
    public void insertNode(MyNode target) {
        if (btree.size() == 0) {
            // initialize tree with first node
            btree[0] = target
            return
        }
        insert(btree[0], target)
    } 
     
    /**
     * private recursive method for node insert, checks value match before insert
     */ 
    private insert(MyNode node, MyNode target) {
        if (target.value < node.value) {
            if (node.leftChild == null) {
                node.leftChild = target
                btree.add(target)
            } else {
                insert(node.leftChild, target)
            }
        } else if (target.value > node.value) {
            if (node.rightChild == null) {
                node.rightChild = target
                btree.add(target)
            } else {
                insert(node.rightChild, target)
            }
        } else {
            println "No insert, found matched node: $node"
        }
    }
    
    public boolean deleteNode(MyNode node) {
        if (!btree) return false
        if (node == null) return false
        
        MyNode p = getParentNode(node)
        println "got parent: $p"
            
        if (node.leftChild == null && node.rightChild == null) {
            if (p.leftChild == node) {
                p.leftChild = null
            } else if (p.rightChild == node) {
                p.rightChild = null
            }
            btree.remove(node)
        } // only have left child, then use left child to replace itself 
        else if (node.leftChild != null && node.rightChild == null) {
            if (p.leftChild == node) {
                p.leftChild = node.leftChild
            } else if (p.rightChild == node) {
                p.rightChild = node.leftChild
            }
            btree.remove(node)
        } // only have right child, then use right child to replace itself  
        else if (node.leftChild == null && node.rightChild != null) {
            if (p.leftChild == node) {
                p.leftChild = node.rightChild
            } else if (p.rightChild == node) {
                p.rightChild = node.rightChild
            }
            btree.remove(node)
        } // have both left and right child, then use either max value of left 
          // sub tree or min value of right sub tree to replace its value,
          // then delete the corresponding max or min node. 
          // Note in this case the current node is NOT deleted, only its value is
          // replaced.
        else {  
            MyNode leftMax = maxNode(node.leftChild)
            println "for leftChild ${node.leftChild}, got leftMax: $leftMax"
            node.value = leftMax.value // replace value
            deleteNode(leftMax)  // delete left max node from the sub tree
        }
        return true
    }
    
    public MyNode minNode() {
        if (!btree) return null
        return minNode(btree[0])
    }
    
    public MyNode minNode(MyNode node) {
        if (!btree) return null
        if (!node) return minNode(btree[0])
        
        List<MyNode> oList = inOrderWalk(node)
        return oList[0]    
    }
    
    public MyNode maxNode() {
        if (!btree) return null
        return maxNode(btree[0])
    }
    
    public MyNode maxNode(MyNode node) {
        if (!btree) return null
        if (!node) return maxNode(btree[0])
        
        List<MyNode> oList = inOrderWalk(node)
        return oList[oList.size() - 1]    
    }
    
    
    /**
     * Doing in-order walk for the whole tree.
     * @see {@link #inOrderWalk(MyNode node)}
     */
    public List<MyNode> inOrderWalk() {
        if (!btree) return null
        return inOrderWalk(btree[0])
    } 
    
    /**
     * Performs in-order walk starting from the given node.
     * For ordered binary tree this should return an value-ordered node list.
     */
    public List<MyNode> inOrderWalk(MyNode node) {
        if (!btree) return null
        if (!node) return inOrderWalk(btree[0])
        
        List<MyNode> walkList = new LinkedList<MyNode>()
        println "In-order walk tree trace:"
        inOrder(node, walkList)
        return walkList
    }
     
    private inOrder(MyNode node, List<MyNode> walkList) {
        if (node) {
            inOrder(node.leftChild, walkList)
            println "touch node: $node"
            walkList.add(node)
            inOrder(node.rightChild, walkList)
        }
    }
    
    /**
     * Search tree for node with matching value
     */
    public MyNode searchNode(int value) {
        if (!btree) return null
        return search(btree[0], value)
    }
    
    // search recursive method
    private MyNode search(MyNode node, int value) {
        println "search: checking node $node"
        if (node == null) {
            println "search: match not found"
            return null
        }
        // based on ordered binary tree definiton, 
        if (value < node.value) {
            search(node.leftChild, value)
        } else if (value > node.value) {
            search(node.rightChild, value)
        } else {
            println "search: found matching node: $node"
            return node
        }     
    }
    
    
    public MyNode getParentNode(MyNode node) {
        if (!btree) return null
        if (!node) return null
        
        for (MyNode n : btree) {
            if (n.leftChild == node) return n
            if (n.rightChild == node) return n
        }
        return null
    }

    
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ordered Binary Tree:\n")
        btree.each { node -> sb.append "  $node \n" }
        sb.toString()
    }
    
}

def dataList = (10..1).toList().collect { 
    if (it % 3 == 0) {
        it * 10
    } else {
        it * 5
    }    
}


def btree = new OrderedBTree(dataList)
println btree

List<MyNode> walkList = btree.inOrderWalk()
println "walk list:"
walkList.each { println "node: $it, parent => ${btree.getParentNode(it)}" }

MyNode toDel = btree.searchNode(25)
println "to delete: $toDel"
btree.deleteNode(toDel)
println btree
walkList = btree.inOrderWalk()
println "walk list:"
walkList.each { println "node: $it, parent => ${btree.getParentNode(it)}" }

