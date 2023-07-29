package model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.Date

const val CLUSTER_PROPERTIES_COLLECTION_NAME = "cprop"


@Serializable
@ExperimentalSerializationApi
data class CouchbaseConnection constructor(
    var connectionName: String,
    var connectionString: String,
    var username: String,
    var password: String,
    var bucket: String,
    var scope: String,
    var collection: String? = null,
    @Serializable(with = DateSerializer::class)
    var createdOn: Date? = null,
)
{
    fun toJson(): String {
        return Json.encodeToString(this)
    }
}
