package db

import groovy.sql.Sql
import oracle.jdbc.driver.OracleTypes
import  oracle.sql.TIMESTAMP


def mt_paid = 'MENU0000000000154803'  // vodmgNationalMenu
def node_num



def db = Sql.newInstance(
//             'jdbc:oracle:thin:@localhost:1521:orcl',
             'jdbc:oracle:thin:@10.253.15.8:1521:udbha01',
             'bui', 'bui', 'oracle.jdbc.driver.OracleDriver'
         )


// API get_all_menus
//db.call("""
//    begin
//        menu_browse.get_all_menus(${Sql.resultSet OracleTypes.CURSOR});
//    end;
//"""
//) { all_menus ->
//    all_menus.eachRow() {
//        println "mt_paid: ${it.mt_paid}, menu_name: ${it.mt_menu_name}, market: ${it.mt_market}"
//    }
//}


// API get_all_top_node_folders
//db.call("""
//    begin
//        menu_browse.get_all_top_node_folders(${Sql.resultSet OracleTypes.CURSOR}, ${mt_paid});
//    end;
//"""
//) { all_node_folders ->
//    println 'returning all top-level node-folders:'
//    def i=1
//    all_node_folders.eachRow() {
//        println " ${i++}   mt_paid: ${it.mt_paid}, node_num: ${it.mn_node_num}, is_leaf: ${it.is_leaf}, cf_paid: ${it.cf_paid}, cf_full_path: ${it.cf_full_path}"
//    }
//}


// API get_all_child_node_folders
//node_num = '013'  // movies&events
//db.call("""
//    begin
//        menu_browse.get_all_child_node_folders(${Sql.resultSet OracleTypes.CURSOR}, ${mt_paid}, ${node_num});
//    end;
//"""
//) { all_node_folders ->
//    println "returning all child node-folders of parent node-folder with mt_paid - node_num: ${mt_paid} - ${node_num}:"
//    def i=1
//    all_node_folders.eachRow() {
//        println " ${i++}   mt_paid: ${it.mt_paid}, node_num: ${it.mn_node_num}, is_leaf: ${it.is_leaf}, cf_full_path: ${it.cf_full_path}"
//    }
//}


// API get_all_child_items
//node_num = '013.025'   // movies&events/allmovies
//db.call("""
//    begin
//        menu_browse.get_all_child_items(${Sql.resultSet OracleTypes.CURSOR}, ${mt_paid}, ${node_num});
//    end;
//"""
//) { all_items ->
//    println "returning all child items of parent node-folder with mt_paid - node_num: ${mt_paid} - ${node_num}:"
//    def i=1
//    all_items.eachRow() {
//        println " ${i++}   title_paid: ${it.title_paid}, title_category_path: ${it.category_path}, title_brief: ${it.title_brief}, rating: ${it.rating}"
//    }
//}

// API get_node_folder_attributes
node_num = '013.025'   // movies&events/allmovies
db.call("""
    begin
        menu_browse.get_node_folder_attributes(${Sql.resultSet OracleTypes.CURSOR}, ${mt_paid}, ${node_num});
    end;
"""
) { nf_attrs ->
    println "returning attributes of node-folder with mt_paid - node_num: ${mt_paid} - ${node_num}:"
    def i=1
    nf_attrs.eachRow() {
        println " ${i++}   parent_node_num: ${it.parent_mn_node_num}"
        println " ${i++}   mn_usage_type: ${it.mn_usage_type}"
        println " ${i++}   cf_paid: ${it.cf_paid},   cf_alias: ${it.cf_alias},   cf_update_num: ${it.cf_update_num}"
        println " ${i++}   cf_scope: ${it.cf_scope},   cf_market: ${it.cf_market}"
        println " ${i++}   cf_is_saved_programs: ${it.cf_is_saved_programs},   cf_is_bookmarks: ${it.cf_is_bookmarks}"
        println " ${i++}   pma_paid: ${it.pma_paid},   pma_update_num: ${it.pma_update_num}"
        println " ${i++}   pma_vw_start_dttm / pma_vw_end_dttm: ${it.pma_vw_start_dttm.toDate()} / ${it.pma_vw_end_dttm.toDate()},   pma_vw_time_mode: ${it.pma_vw_time_mode}"
        println " ${i++}   cf_full_path: ${it.cf_full_path}"
        println " ${i++}   cf_full_path: ${it.cf_full_path}"

    }
}


