package database.repository

import model.CouchbaseConnection


interface CouchbaseConnectionRepository {
    fun getByConnectionname(
        connectionName: String): List<CouchbaseConnection>

    fun get(): List<CouchbaseConnection>

    fun couchbaseConnectionCount(): Int
    fun save(couchbaseConnection: CouchbaseConnection) : Boolean
}
