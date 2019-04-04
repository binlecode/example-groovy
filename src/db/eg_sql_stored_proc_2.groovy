package db

@GrabResolver(name = 'custom', root = 'http://www.datanucleus.org/downloads/maven2/', m2Compatible = 'true')
@Grab(group = 'com.oracle', module = 'ojdbc6', version = '11.2.0.3')


import groovy.sql.OutParameter
import groovy.sql.Sql
import oracle.jdbc.driver.OracleTypes

import java.sql.ResultSet

def sql = Sql.newInstance(
        'jdbc:oracle:thin:@(description=(address=(host=...)(protocol=tcp)(port=1527))(connect_data=(service_name=...)))',
        '...', '...',
        'oracle.jdbc.driver.OracleDriver'
)

Map params = [
        dr: '12345678',
        tc: '@@@'
]

/*
the package to call has the following pks specs:

create or replace PACKAGE PKG_ABC
IS
TYPE t_sbp_hist IS RECORD
   (dodge_proj_nbr       dph_dps_queue_history.dodge_proj_nbr%TYPE
   ,title_code           dph_dps_queue_history.title_code%TYPE
   ,addenda_nbr          dph_dps_queue_history.addenda_nbr%TYPE
   ,db_name              VARCHAR2(100)
   ,db_action            VARCHAR2(100)
   ,db_timestamp         DATE
   ,line_order           NUMBER);
TYPE c_sbp_hist IS REF CURSOR RETURN t_sbp_hist;

PROCEDURE SP_ABC
         (p_dodge_proj_nbr IN     dph_dps_queue_history.dodge_proj_nbr%TYPE
         ,p_title_code            dph_dps_queue_history.title_code%TYPE
         ,p_addenda_nbr           dph_dps_queue_history.addenda_nbr%TYPE
         ,p_sbp_hist          OUT c_sbp_hist
         ,p_sqlcode           OUT NUMBER
         ,p_sqlerrm           OUT VARCHAR2);
--------------------------------------------------------------------------------------------
--==========================================================================================
--========================================================================================--
END PKG_ABC;

That means the returned ref cursor has custom oracle type.

 */

/**
 * To get REF CURSOR from Oracle, groovy.sql.Sql doesn't have matching OutParameter for the custom type.
 * Therefore we need to make a custom CURSOR_PARAMETER to pass to the stored procedure call.
 * @see {@link groovy.sql.OutParameter}
 */
OutParameter CURSOR_PARAMETER = new OutParameter() {
    public int getType() {
        return OracleTypes.CURSOR
    }
}

// calling statement with inline bind variables

sql.call("""
    BEGIN
        PKG_XYZ.SP_SBP_HISTORY(
            ${params.dr},
            ${params.tc},
            'STD PACK',
            ${CURSOR_PARAMETER},
            ${Sql.INTEGER},
            ${Sql.VARCHAR}
        );
    END;
""") { ResultSet rs, errCode, errMsg ->
    println "errCode=${errCode}, errMsg=${errMsg}"
    String logLine
    def rowNum = 0
    rs.eachRow {
//        println "row ${rowNum++}: $it"
        logLine = "row ${rowNum++}: "
        logLine += " DR #: ${it.getString('DR #')},"   // have to explicitly call ResultSet.getXxx() to get value
        logLine += " Title: ${it.getString('Title')},"
        logLine += " Add #: ${it.getString('Add #')},"
        logLine += " DB: ${it.getString('DB')},"
        logLine += " Action: ${it.getString('Action')},"
        logLine += " Timestamp: ${it.getDate('Timestamp')},"
        logLine += " Order: ${it.getString('Order')}"
        println logLine
    }
}

// this is another way of calling using positioned bind-variables
/*
sql.call('''
        BEGIN
            PKG_SBP_HISTORY.SP_SBP_HISTORY(?,?,?,?,?,?);
        END;
        ''',
        [
                '201800531208',
                '@@@',
                'STD PACK',
                CURSOR_PARAMETER,
                Sql.INTEGER,
                Sql.VARCHAR
        ]
) { cursorResults, errCode, errMsg ->
    println "errCode=${errCode}, errMsg=${errMsg}"
    def rowNum = 0
    def logLine = ''
    cursorResults.eachRow {
        println "row ${rowNum++}: $it"
        logLine = "row ${rowNum++}: "
        logLine += " DR #: ${it.getString('DR #')},"
        logLine += " Title: ${it.getString('Title')},"
        logLine += " Add #: ${it.getString('Add #')},"
        logLine += " DB: ${it.getString('DB')},"
        logLine += " Action: ${it.getString('Action')},"
        logLine += " Timestamp: ${it.getDate('Timestamp')},"
        logLine += " Order: ${it.getString('Order')}"
        println logLine
    }
}
*/




