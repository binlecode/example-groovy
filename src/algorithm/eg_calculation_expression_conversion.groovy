
// stack based calculation expression parsing
calStr = '( 3 + 3.14 / ( 17 - 45 ) ) * 12 - 22 / 33'.tokenize()


// compare the priority of operators
def compOp = { op1, op2 ->
    if (op1 == '+' || op1 == '-') {
        op1 = 1
    } else {
        op1 = 2
    }
    if (op2 == '+' || op2 == '-') {
        op2 = 1
    } else {
        op2 = 2
    }
    // if return -1, op1 < op2, if return 1, op2 > op2, else 0, op1 == op2
    op1 - op2
}

def isOp = { elm ->
    elm instanceof String && (
        elm == '+' || 
        elm == '-' || 
        elm == '*' || 
        elm == '/'
    )
}

def isPr = { op ->
    op == '(' || op == ')'
}


Stack st = []
def expr = []
for (def chr in calStr) {
    println "getting char $chr"
    println "current stack: $st"
    if (!isOp(chr) && !isPr(chr)) {
        expr += chr
    } else if (isPr(chr)) {
        if (chr == '(') {
            st.push(chr)
        } else if (chr == ')') {
            def op = st.pop()
            while (isOp(op)) {  // final iteration in the loop will remove the stacked '('  
                expr += op
                op = st.pop()  
            }
        }
    } else {    // is operator
        if (st.size() == 0) {
            st.push(chr)
        } else {
            def op = st.peek()
            println "got stack peek: $op"
            if (op == '(') {
                st.push(chr)  // push current op into stack
            } else {
                println "comparing $op with $chr: ${compOp(op, chr)}"
                
                // release all previous ops that are higher or equal to current op
                while (isOp(op) && compOp(op, chr) >= 0) {
                    expr += st.pop()
                    if (st.size() == 0) {
                        break
                    }
                    op = st.peek()
                }
                st.push(chr)            
            }
        }
    } 

      
    println ">> expr: $expr"  
    println ">> stack: $st"  
}
// tail append all remaining ops in the stack  
while (st.size() > 0) {
    expr += st.pop()
}

expr



