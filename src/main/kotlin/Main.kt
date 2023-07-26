import org.extism.sdk.Context
import org.extism.sdk.Plugin
import org.extism.sdk.manifest.Manifest
import org.extism.sdk.wasm.WasmSourceResolver
import java.nio.file.Path

fun main(args: Array<String>) {

    //! Load the wasm file
    //! Get an instance of the plugin
    val resolver = WasmSourceResolver()
    val manifest = Manifest(resolver.resolve(Path.of(System.getenv("WASM_FILE"))))
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

    //println("Program arguments: ${args.joinToString()}")
}