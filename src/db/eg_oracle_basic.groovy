@GrabResolver(name = 'custom', root = 'http://www.datanucleus.org/downloads/maven2/', m2Compatible = 'true')
@Grab(group = 'com.oracle', module = 'ojdbc6', version = '11.2.0.3')


import groovy.sql.Sql
import groovy.sql.GroovyRowResult
import java.sql.Timestamp

import oracle.jdbc.OracleDriver
import oracle.sql.TIMESTAMP


def sql = Sql.newInstance("jdbc:oracle:thin:@10.253.15.8:1521:udbha01",
    'bui', 'bui', 'oracle.jdbc.OracleDriver')

//
def rs  // result set holder
// Class GroovyRowResult has keySet() method
def rows = sql.rows('select * from rpt_folder_attrs_vw where rownum < 10')
if (rows) {
    def firstRow = rows.get(0)
    def keySet = firstRow.keySet()
    keySet.each() { k ->
        println k + ': ' + firstRow[k] + ', class: ' + firstRow[k].getClass().getName()
    }

    def v // hold oracle field obj
    def val // hold java field obj
    rows.each() { row ->
        keySet.each() { k ->
            v = row[k]
            if (v instanceof oracle.sql.TIMESTAMP) {
                val = v.toTimestamp()  // convert from oracle.Timestamp to java.Timestamp
            } else {
                val = v
            }
            print k + ': ' + val + ' '
        }
    print '\n'

    }


} else {
    println 'no record found'
}






