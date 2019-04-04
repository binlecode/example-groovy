
// stack based calculation expression parsing

calString = [3, 4, '+', 12, '*', 22, 33, '/', '-']

def isOp = { elm ->
    elm instanceof String && (
        elm == '+' || elm == '-' || elm == '*' || elm == '/'
    )
}

def op = { v1, v2, opChar ->
    println "calculate: $v2  $opChar  $v1"
    switch (opChar) {
        case '+':
            return v2 + v1
        case '-':
            return v2 - v1
        case '*':
            return v2 * v1
        case '/':
            return v2 / v1
    }
}

Stack st = []

for (def e in calString) {
    if (!isOp(e)) {
        st.push(e)    
    } else {
        def t = op(st.pop(), st.pop(), e)
        st.push(t) 
    }    
}

println st

