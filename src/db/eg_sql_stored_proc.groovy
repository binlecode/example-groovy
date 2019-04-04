package db

@GrabResolver(name = 'custom', root = 'http://www.datanucleus.org/downloads/maven2/', m2Compatible = 'true')
@Grab(group = 'com.oracle', module = 'ojdbc6', version = '11.2.0.3')


import groovy.sql.Sql
import oracle.jdbc.driver.OracleTypes
import oracle.sql.TIMESTAMP

def sql = Sql.newInstance(
     'jdbc:oracle:thin:@10.253.15.21:1521:dev5',
     'mms', 'mms', 
     'oracle.jdbc.driver.OracleDriver'   
)



// example of sp call with string output

def call = """
    begin
        BUI.MENU_BROWSE.get_title_count_by_path(
            ${Sql.VARCHAR},  -- out_title_count out varchar2
            ${cfFullPath}
        );
    end;
"""
println call

db.call(call) {out_title_count  ->
    println "db stored proc return title count: ${out_title_count}"
    allChildTitlesCount = out_title_count
}
println "child titles under folder: ${cfFullPath}, count: ${allChildTitlesCount}"



// example of sp call with refcursor output and some string datetime

def mtAssetId = 'MENU0000000000144718'
def startDate = '2009-10-10'
def endDate = '2011-10-10'
def callStmt = """
    begin 
      menu_reports.menu_summary_report(
        ${Sql.resultSet OracleTypes.CURSOR},
        ${mtAssetId},
        to_date(${startDate}, 'yyyy-mm-dd'),
        to_date(${endDate}, 'yyyy-mm-dd')
      ); 
    end;
"""
println callStmt
sql.call(callStmt) { rs ->
    rs.eachRow() {
        println "$it.action_date : ${it.action} -> ${it.full_path}" 
    }
}

// example of sp call with refcusor out and some java SQL type input

def mtPaid = 'MENU0000000000002546'
def timestamp = null
def licWindowFlag = 'O'
call = """
    begin
       report.rpt_titles_lic_window (
        ${Sql.resultSet OracleTypes.CURSOR},   -- out sys_refcursor,
        ${licWindowFlag},  --  in varchar2,  -- I: in-window, O: out-window
        ${timestamp},     -- in timestamp,  -- if null use current systimestamp (UTC)
        ${mtPaid}     -- IN varchar2   -- if null return titles for all menu trees
      );
    end;
"""
println call
db.call(call) { out_titles ->
    def i = 1
    println "return titles:"
    out_titles.eachRow() {
        println "${i++}: title_paid: ${it.title_paid}, lic_win_start: ${it.licensing_window_start}"
        if (i>10) {
            return
        }
    }
}

// another way to loop returned refcursor is to use Interator interface
db.call(call) { out_titles ->
    def i = 1
    def it
    println "return folders:"
    while (out_titles.next()) {
        if (i > 1000) {break}
        it = out_titles
        println "${i++}: cfPath: ${it.cf_full_path}, nodeNum: ${it.mn_node_num}, titleCount: ${it.title_count}"
    }

    //out_titles.eachRow() {
    //    println "${i++}: cfPath: ${it.cf_full_path}, nodeNum: ${it.mn_node_num}, titleCount: ${it.title_count}"
    //}
}


// some more type conversion for returned oracle timestamp types
def nodeFlag = 'Y'   // Y, N, or null
def serviceIdFlag // = 'N'   // Y, N, or null
call = """
    begin
       report.rpt_folder_node (
        ${Sql.resultSet OracleTypes.CURSOR},   -- out sys_refcursor,
        ${nodeFlag},   -- in varchar2,
        ${serviceIdFlag}, -- in varchar2,
        ${mtPaid}  --  in varchar2,
      );
    end;
"""
println call
db.call(call) { rs ->
    def i = 1
    println "return folders:"
    while (rs.next()) {
        if (i > 100) {break}

        println "${i++}: cfPath: ${rs.cf_full_path}, nodeNum: ${rs.mn_node_num}, serviceId: ${rs.content_service_id}, vwStart: ${rs.pma_vw_start_dttm.stringValue()}, vwEnd: ${rs.pma_vw_end_dttm.stringValue()}"
    }
}


  
