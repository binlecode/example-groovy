

def writer = new StringWriter()
// Markup is usually generated for formatted output, the printing 
// is done implicitly as soon as the construction is finished. 
// To make this possible, a Writer is passed into the 
// MarkupBuilder�s constructor
def builder = new groovy.xml.MarkupBuilder(writer)
builder.setDoubleQuotes(true)

builder."myns:rn"(
        'xmlns:myns': 'myname.com:space',  // this is how we define namespaces
        'xmlns:myns2': 'ns2.com:space',
        customKey: 'custom value'
) {
    todo(id: "1") {
        name "todo number 1"
        name "todo number 2"
        note "todo note 1"
        'myns2:subTodo'(id: "1.1") {
            subName "todo sub number 1.1"
        }
    }
}

// keep a good habit of getting rid of windows extra line return
println writer.toString().replaceAll('\r', '')

def root2 = new XmlSlurper().parseText(writer.toString())
for (i in 0 .. root2.todo.size()) {
    println root2.todo[i].@id.text()
    println root2.todo[i].name.text()
//    println root2.todo[i].note.getClass().getName()
    println root2.todo[i].subTodo.@id.text()
}

def root3 = new XmlParser().parseText(writer.toString())
println root3.todo.size()  // this gives the nodeList size 
println root3.todo[0].value().size()   // this gives todo[0]'s direct child list size
println root3.todo[0].subTodo[0]?.value()
println root3.todo[0].subTodo[0]?.text()



def ts = new XmlSlurper().parseText('''
<MT1_Item itemId="c38b81e7-1ff2-44d8-8900" 
    itemType="TS" 
    itemName="ClosedStatus">
  <ItemBlob>
  </ItemBlob>
</MT1_Item>
''')

println ts.@itemId.text()
