def DATE_TIME_PATTERN = 'yyyy-MM-dd HH:mm:ss'     
    
def defaultDynamicSqlConfigMap = [
    name: 'mmsMenuChange',


    fieldMap: [
        userId:     [table: 'MMS_CHANGE', column: 'USER_ID'],           // default type is string
        changeSid:  [table: 'MMS_CHANGE', column: 'CHANGE_SID'],
        changeTime: [table: 'MMS_CHANGE', column: 'CHANGE_TIME', type: 'timestamp'],
        objectType: [table: 'MMS_CHANGE', column: 'OBJECT_TYPE'],
        objectId:   [table: 'MMS_CHANGE', column: 'OBJECT_ID'],
        changeType: [table: 'MMS_CHANGE', column: 'CHANGE_TYPE'],
        changeDesc: [table: 'MMS_CHANGE', column: 'CHANGE_DESC'],
        fullPath:   [table: 'CATEGORY_FOLDER', column: 'FULL_PATH']
    ],

    // if only one table in fieldSet definition, this can be skipped
    tableMap: [
        MMS_CHANGE: [:],
        CATEGORY_FOLDER: [ 
            join: 'LEFT OUTER JOIN', 
            joinColumnMap: [
                [column: 'CF_ASSET_ID', refTable: 'MMS_CHANGE', refColumn: 'OBJECT_ID']
                //[column: 'DUMMY_COL', refTable: 'MMS_CHANGE', refColumn: 'DUMMY_REF_COL']
            ]
        ]
    ],

    // supported orders must use column names
    sortFieldSet: ['user_id', 'change_time'],
    
    // default sortOrder
    _sortOrder: 'asc',  // or 'desc'
     
    // default query mode
    _queryMode: 'rows', // 'rows' or 'firstRow'
    
    // default frame boundary for return result set
    _frameStart: 1,
    _frameSize: 2000
]

def c = defaultDynamicSqlConfigMap

def filterOpMap = [
    like:        'LIKE',
    notLike:     'NOT LIKE',
    eq:          '=',
    notEq:       '<>',
    lt:          '<',
    gt:          '>',
    le:          '<=',
    ge:          '>=',
    isNull:      'IS NULL',
    isNotNull:     'IS NOT NULL'
]

def sortOpMap = [
    asc:        'ASC',
    desc:       'DESC'
]



// add table alias
c.tableMap.eachWithIndex { t, idx ->
    t.value.alias = "t_${idx}_"
}

// generate select clause
def selectClause = '''SELECT
'''
c.fieldMap.eachWithIndex { f, idx ->
    selectClause += "${c.tableMap[f.value.table].alias}.${f.value.column} AS ${f.key}"
    if (idx < c.fieldMap.size() - 1) {
        selectClause += ',\n'
    } else {
        selectClause += '\n'
    }  
}


// generate from clause
def fromClause = '''FROM
'''
c.tableMap.eachWithIndex { t, idx ->
    if (idx == 0) {
        fromClause += """  ${t.key} ${t.value.alias}
""" } else {
        fromClause += """  ${t.value.join} ${t.key} ${t.value.alias} 
    ON 
"""
        t.value.joinColumnMap.eachWithIndex { jc, idxx -> 
            if (idxx > 0) {
                fromClause += '''    AND
'''
            }
            fromClause += """    ${t.value.alias}.${jc.column} = ${c.tableMap[jc.refTable].alias}.${jc.refColumn}
"""
        }
    
    }
}






// all the values from web requests are of String format
def inputParamsMap = [
    objectType: 'CateogryFolder'
]
def inputFilterSet = [
    [field: 'objectType', op: 'notEq'],
    [field: 'fullPath', op: 'isNotNull']    
]
def inputSortFieldList = ['fullPath', 'userId']
def inputSortOrder = 'desc'



// generate where clause
def whereClause 
inputFilterSet.eachWithIndex { f, idx -> 
    if (idx == 0) {
        whereClause = '''WHERE
'''
    } else {
        whereClause += '''  AND
'''
    }
    switch (f.op) {
        case 'like':
        case 'notLike':
        case 'eq':
        case 'notEq':
        case 'lt':
        case 'gt':
        case 'le':
        case 'ge':
             whereClause += """  ${c.tableMap[c.fieldMap[f.field].table].alias}.${c.fieldMap[f.field].column} ${filterOpMap[f.op]}  :${f.field}
"""
            break;
        case 'isNull':
        case 'isNotNull':
              whereClause += """  ${c.tableMap[c.fieldMap[f.field].table].alias}.${c.fieldMap[f.field].column} ${filterOpMap[f.op]}
"""
            break;
        default:
            null;  // do nothing           
    }
}

//generate sort clause
def sortClause
if (inputSortFieldList?.size() > 0) {
    def sortFieldList = (c.fieldMap.findAll {f -> inputSortFieldList.contains(f.key)})*.key
    sortClause = """ORDER BY ${sortFieldList.join(', ')} ${sortOpMap[inputSortOrder] ?: ''}
"""  
}

println selectClause + fromClause + (whereClause ?: '') + (sortClause ?: '')

