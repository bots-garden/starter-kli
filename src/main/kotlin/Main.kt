import com.couchbase.lite.Blob
import com.couchbase.lite.MutableDocument
import database.CBLite
import database.repository.CouchbaseConnectionRepositoryDb
import model.CouchbaseConnection
import model.ExtismPlugin
import org.extism.sdk.Context
import org.extism.sdk.manifest.Manifest
import org.extism.sdk.wasm.WasmSourceResolver
import java.net.URL
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.name

fun main(args: Array<String>) {

    var cblite = CBLite()
    cblite.initDB()
    var repo = CouchbaseConnectionRepositoryDb(cblite)
    var p = CouchbaseConnection("MyCo","couchbase://localhost",
        "Administrator","password","defaul","_default")

    repo.save(p)
    var connections = repo.get()
    var count = repo.couchbaseConnectionCount()
    connections.forEach { co -> println(co.connectionName) }
    println(count)
    //println("Program arguments: ${args.joinToString()}")
    var filePath = Path.of(System.getenv("WASM_FILE"))
    var fileUrl = filePath.toUri().toURL()
    var pluginBlob = Blob("application/wasm", fileUrl)

    var pluginId = "myPluginId"
    var mutableDoc = MutableDocument(pluginId)
        .setBlob("wasmbinary",pluginBlob)
        .setString("filename", filePath.name)
        .setString("name","simple plugin")
    cblite.extismPluginCollection?.save(mutableDoc)

    var storedDoc = cblite.extismPluginCollection?.getDocument(pluginId)
    var blob = storedDoc?.getBlob("wasmbinary")


    //! Load the wasm file
    //! Get an instance of the plugin
    val resolver = WasmSourceResolver()
    val manifest = Manifest(resolver.resolve(storedDoc?.getString("filename"), blob?.content))
    val extismCtx = Context()
    val plugin = extismCtx.newPlugin(manifest, true, null)

    // org.extism.sdk.HostFunction[]

    try {
        val output = plugin.call("handle", "Bob Morane")
        println(output)
    } catch (e: Exception) {
        // handler
        println("🤬" + e.message)
    }

}
