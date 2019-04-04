package mongodb

@Grab(group='org.mongodb', module='mongo-java-driver', version='2.13.2')

import com.mongodb.BasicDBObject
import com.mongodb.BulkWriteOperation
import com.mongodb.BulkWriteResult
import com.mongodb.Cursor
import com.mongodb.DB
import com.mongodb.DBCollection
import com.mongodb.DBCursor
import com.mongodb.DBObject
import com.mongodb.MongoClient
import com.mongodb.ParallelScanOptions
import com.mongodb.ServerAddress


println 'driver loaded ok, imports ok'



import static java.util.concurrent.TimeUnit.SECONDS


// When a primary goes down, the driver will automatically find the new primary (once
// one is elected) and will route requests to it as soon as possible. However, while there is
// no reachable primary your application will be unable to perform writes.


// To directly connect to a single MongoDB server (note that this will not auto-discover the primary even
// or
//MongoClient mongoClient = new MongoClient( "192.168.56.10" , 27017 )
// or, to connect to a replica set, with auto-discovery of the primary, supply a seed list of members
MongoClient mongoClient = new MongoClient(Arrays.asList(new ServerAddress("192.168.56.10", 27017),
                                      new ServerAddress("192.168.56.11", 27017),
                                      new ServerAddress("192.168.56.12", 27017)))

DB db = mongoClient.getDB( "test" )

println 'mongodb test ok'

DBCollection users = db.getCollection('users')
println users.findOne()
