package database

import com.couchbase.lite.*
import com.couchbase.lite.Collection
import com.couchbase.lite.internal.CouchbaseLiteInternal
import model.CLUSTER_PROPERTIES_COLLECTION_NAME
import model.CouchbaseConnection
import java.io.File
import java.nio.file.Paths

const val APPLICATION_NAME = "starter-kli"

class CBLite {

    var couchbaseConnectionCollection: Collection? = null


    public fun initDB() {
        val homePath = System.getProperty("user.home")
        val configurationPath = String.format("%s%s%s_config", homePath, File.separator, APPLICATION_NAME)
        val path = Paths.get(configurationPath)
        var configDir = path.toFile()
        // One-off initialization
        CouchbaseLite.init(false, configDir, File(configDir, CouchbaseLiteInternal.SCRATCH_DIR_NAME));
        println("CBL Initialized");

        //turn on uber logging - in production apps this shouldn't be turn on
        Database.log.console.domains = LogDomain.ALL_DOMAINS
        Database.log.console.level = LogLevel.INFO

        // Create a database
        var database = Database(APPLICATION_NAME);
        println("Database created: " + APPLICATION_NAME);

        // Create a new collection (like a SQL table) in the database.
        couchbaseConnectionCollection = database.createCollection(CLUSTER_PROPERTIES_COLLECTION_NAME);
        println("Collection created: " + couchbaseConnectionCollection);

        var prop = CouchbaseConnection("MyCo","couchbase://localhost",
            "Administrator","password","defaul","_default")

//        // Create a new document (i.e. a record)
//        // and save it in a collection in the database.
//        var mutableDoc = MutableDocument(null, prop.toJson())
//        this.couchbaseConnectionCollection.save(mutableDoc);
//
//        // Retrieve immutable document and log the database generated
//        // document ID and some document properties
//        var document = couchbaseConnectionCollection.getDocument(mutableDoc.getId());
//        if (document == null) {
//            println("No such document :: " + mutableDoc.getId());
//        }
//        else {
//            println("Document ID :: " + document.getId());
//            println("Learning :: " + document.getString("username"));
//        }
//
//
//        // Retrieve and update a document.
//        document = couchbaseConnectionCollection.getDocument(mutableDoc.getId());
//        if (document != null) {
//            couchbaseConnectionCollection.save(document.toMutable().setString("username", "Kotlin"));
//        }
//
//        // Create a query to fetch documents with username == "Kotlin"
//        var query = QueryBuilder.select(SelectResult.all())
//            .from(DataSource.collection(couchbaseConnectionCollection))
//            .where(
//                Expression.property("username")
//                .equalTo(Expression.string("Kotlin")));
//
//
//        query.execute().use { rs -> System.out.println("Number of rows :: " + rs.allResults().size) }

    }
}
