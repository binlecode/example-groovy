@GrabConfig(systemClassLoader=true)
@Grab(group='mysql', module='mysql-connector-java', version='5.1.6')

import groovy.sql.Sql

def sql = Sql.newInstance(
    'jdbc:mysql://localhost:3306/gtunes_ds1',
    'root', '',
    'com.mysql.jdbc.Driver' 
)

sql.firstRow('select count(1) as count from artist').count

sql.eachRow('select * from artist') {
    println it
}

