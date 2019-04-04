package db

import groovy.sql.Sql
import oracle.jdbc.driver.OracleTypes

def sql = Sql.newInstance(
     //'jdbc:oracle:thin:@localhost:1521:orcl',
     'jdbc:oracle:thin:@10.253.15.21:1521:dev5',
     'mms', 'mms', 
     'oracle.jdbc.driver.OracleDriver'   
)

def cfAssetId = 'MENU0000000000001180'
def ntlMtAssetId = null // 'MENU0000000000140505'
def locMtAssetId = null //'MENU0000000000162829'
def deltaFolderList = []

def callStmt = """
    begin 
      delta_publish.get_delta_folders(
        ${Sql.resultSet OracleTypes.CURSOR},
        ${Sql.VARCHAR},
        ${Sql.VARCHAR},
        ${cfAssetId},
        ${ntlMtAssetId},
        ${locMtAssetId}
      ); 
    end;
"""
println callStmt
sql.call(callStmt) { rs, outCode, outMsg ->
    println "return code: $outCode"
    println "return message: $outMsg"
    
    if ('0'.equals(outCode)) {
        rs.eachRow() {
            println "${it.parent_node_id} : ${it.node_id} : ${it.sib_order} : ${it.full_path_display}"
            deltaFolderList << [full_path_display: it.full_path_display]  
        }
    }
}

println "delta folder list size: ${deltaFolderList.size()}"
deltaFolderList.each() { println it }
  
        