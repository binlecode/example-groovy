package db

import groovy.sql.Sql
import oracle.jdbc.driver.OracleTypes
import java.sql.Connection;
import java.sql.PreparedStatement;

def db = Sql.newInstance(
     //'jdbc:oracle:thin:@localhost:1521:orcl',
     'jdbc:oracle:thin:@10.252.240.136:1521:udbha01',
     'nav', 'nav', 
     'oracle.jdbc.driver.OracleDriver'   
)

/*
def sqlQuery = """
    SELECT account_number, outlet, component, XMLAGG(xmlelement(e, rate_code||' ') ORDER BY rate_code).extract('//text()') AS rate_codes 
    FROM outlet_rate_codes 
    WHERE account_number = '0951300000101'
      AND corp = CASE WHEN LENGTH( '0951300000101' ) = 16 THEN SUBSTR( '0951300000101' , 1, 6) 
                 ELSE SUBSTR( '0951300000101' , 1, 5) END GROUP BY account_number, outlet, component 
"""
println db.rows(sqlQuery)
*/

def accountNumber = '0951300000101'

def conn = db.getConnection();

//def sqlQuery = 'select length(?) as str, substr(?, 1, 6) as sub_str from dual'
/*
def sqlQuery = "SELECT account_number, outlet, component, " +
    "XMLAGG(xmlelement(e, rate_code||' ') ORDER BY rate_code).extract('//text()') AS rate_codes " +
    "FROM outlet_rate_codes " +
    "WHERE account_number = ? " +
       "AND corp = CASE WHEN LENGTH( ? ) = 16 THEN SUBSTR( ? , 1, 6) " +
                  "ELSE SUBSTR( ? , 1, 5) END " +
     "GROUP BY account_number, outlet, component "
*/
def sqlQuery = """
    SELECT account_number, outlet, component, XMLAGG(xmlelement(e, rate_code||' ') ORDER BY rate_code).extract('//text()').getStringVal() AS rate_codes 
    FROM outlet_rate_codes 
    WHERE account_number = '0951300000101'
      AND corp = CASE WHEN LENGTH( '0951300000101' ) = 16 THEN SUBSTR( '0951300000101' , 1, 6) 
                 ELSE SUBSTR( '0951300000101' , 1, 5) END GROUP BY account_number, outlet, component 
"""

println sqlQuery

def ps = conn.prepareStatement(sqlQuery)
//(1..2).each() {
//    ps.setString(it, accountNumber)
//}


def rs = ps.executeQuery();

while (rs.next()) {
    //println rs.getString('str') + ' , substr: ' + rs.getString('sub_str')
    


    println rs.getString('account_number') + ' : ' + rs.getString('outlet') + ' : ' + rs.getString('rate_codes')
}





        
        
        