


def builder = new groovy.xml.MarkupBuilder(new StringWriter())
builder.setDoubleQuotes(true)

def fileName = 'eg_xmlparser_jspr'

def jr = new XmlParser().parse("./${fileName}.xml")
jr.field.each { field ->
    println field.@name
    //println root2.todo[i].name.text()
//    println root2.todo[i].note.getClass().getName()
    //println root2.todo[i].subTodo.@id.text()
}

queryString = jr.queryString[0].value()
println "queryString: \n $queryString \n"

println jr.title.band.staticText.reportElement.@style

def staticTextAtX0 = jr.columnHeader.band.staticText[0]
// .value() method is for target element's enclosing content
println staticTextAtX0.value()

def re = jr.columnHeader.band.staticText.reportElement.find {
    it.@x == "0"
}
println re
// .attributes() is for target element's attributes in hash map format
println re.attributes()


// modify xml and write to file

staticTextAtX0.appendNode('staticText', [reportElement: staticTextAtX0.reportElement])

def stringWriter = new StringWriter()
def printWriter = new PrintWriter(stringWriter)
def xmlNodePrinter = new XmlNodePrinter(printWriter)

xmlNodePrinter.print(jr)
def newXmlString = "<?xml version='1.0' encoding='latin1'?>\n" + stringWriter.toString()
println 'print the revised xml: \n'
println newXmlString


// write to file
println "writing file: ${fileName}_updated.xml  \n"
file = new File("${fileName}_updated.xml")
file.write(newXmlString)


return 0


