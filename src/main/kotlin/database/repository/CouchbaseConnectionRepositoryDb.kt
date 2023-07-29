package database.repository

import com.couchbase.lite.*
import com.couchbase.lite.Function
import database.CBLite
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import model.CLUSTER_PROPERTIES_COLLECTION_NAME
import model.CouchbaseConnection
import java.lang.RuntimeException
import kotlin.Exception
import kotlin.Int
import kotlin.String
import kotlin.let
import kotlin.stackTraceToString

class CouchbaseConnectionRepositoryDb(
    private val databaseManager: CBLite
) : CouchbaseConnectionRepository {
    private val connectionNameAttributeName = "connectionName"

    override fun getByConnectionname(connectionName: String): List<CouchbaseConnection> {
        val couchbaseConnections = mutableListOf<CouchbaseConnection>()
        try {
            val collection = databaseManager.couchbaseConnectionCollection
            collection?.let { database ->
                //search by connectionName
                var whereQueryExpression = Function.lower(Expression.property(connectionNameAttributeName))
                    .like(Expression.string("%" + connectionName.lowercase() + "%"))

                //create query to execute using QueryBuilder API
                val query = QueryBuilder.select(SelectResult.all()).from(DataSource.collection(collection))
                    .where(whereQueryExpression)

                //loop through results and add to list
                query.execute().allResults().forEach { item ->
                    val json = item.getDictionary(
                        CLUSTER_PROPERTIES_COLLECTION_NAME
                    )
                    val couchbaseConnection = Json.decodeFromString<CouchbaseConnection>(json?.toJSON() ?: item.toJSON())
                    couchbaseConnections.add(couchbaseConnection)
                }
            }
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
        return couchbaseConnections
    }

    override fun get(): List<CouchbaseConnection> {

        val locations = mutableListOf<CouchbaseConnection>()
        try {
            val collection = databaseManager.couchbaseConnectionCollection
            collection?.let {
                val query = QueryBuilder.select(SelectResult.all()).from(DataSource.collection(collection))
                query.execute().allResults().forEach { item ->
                    val json = item.getDictionary(
                        CLUSTER_PROPERTIES_COLLECTION_NAME
                    )
                    val couchbaseConnection = Json.decodeFromString<CouchbaseConnection>(json?.toJSON() ?: item.toJSON())
                    locations.add(couchbaseConnection)
                }
            }
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
        return locations
    }

    override fun couchbaseConnectionCount(): Int {
        var resultCount = 0
        val countAliasName = "count"
        try {
            val collection = databaseManager.couchbaseConnectionCollection
            collection?.let {
                val query = QueryBuilder.select(
                    SelectResult.expression(Function.count(Expression.string("*"))).`as`(countAliasName)
                ).from(DataSource.collection(collection))
                val results = query.execute().allResults()
                resultCount = results[0].getInt(countAliasName)
            }
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
        return resultCount
    }

    override fun save(couchbaseConnection: CouchbaseConnection): Boolean {
        try {
            val mutableDocument = MutableDocument(null, couchbaseConnection.toJson())
            val collection = databaseManager.couchbaseConnectionCollection
            collection?.save(mutableDocument)
        } catch (e: CouchbaseLiteException) {
            println(e)
            throw RuntimeException(e)
        }
        return true
    }
}

