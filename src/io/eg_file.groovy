package io

import groovy.sql.Sql



def db = Sql.newInstance(
     //'jdbc:oracle:thin:@localhost:1521:orcl',
     'jdbc:oracle:thin:@10.253.15.15:1521:udbha01',
     //'jdbc:oracle:thin:@76.96.36.36:1521:udbha01',
     'nav', 'nav', 
     'oracle.jdbc.driver.OracleDriver'   
)

println "a textmate test"



//list all files under current folder   
def dirFile = new File(".")
dirFile.eachFile { file ->
    println "${file.getName()}"
}

def inputFileName = "title_assets.txt"
def inputFile = new File(inputFileName)
def outputFile = new File(inputFileName+".log")
if (outputFile.exists()) {outputFile.write("")}

def fieldSetList = []
inputFile.eachLine {
    println it
    itt = it.tokenize('|')
    // use getAt method provided by GDK
    fieldSetList.add([
       path:  itt.getAt(0).replaceAll(" ", "").replaceAll('\\\\', '/').toLowerCase(),
       paid:  itt.getAt(-2).plus(itt.getAt(-1)).replaceAll(" ", "")
    ]) 
}


outputFile << "\t\t *** query result for titles - run at ${new Date()} *** \n\n"

def stmt
fieldSetList.each {
    println it.path + ' | ' + it.paid
    
    //query(it.path, it.paid) 
    
    
    stmt = """
        select title_paid, tc.category_path, ts.status, ts.vod_system_id
        from title_categories tc join titles_status ts using (title_paid)
        where tc.category_path = ${it.path}
            and title_paid = ${it.paid}
    """
    println stmt
    outputFile << "query result for title with Paid: ${it.paid}, and path: ${it.path} \n"
    db.eachRow(stmt) { rs ->
        def i = 1
        println "return service ids:"
        while (rs.next()) {
            if (i > 2000) {break}    
            //println "${i++}: titlePath: ${rs.category_path}, status: ${rs.status}, vod: ${rs.vod_system_id}"
            outputFile << "\t ${i++}: status: ${rs.status}, vod: ${rs.vod_system_id} \n"
            
        }       
    }
    
   
}






