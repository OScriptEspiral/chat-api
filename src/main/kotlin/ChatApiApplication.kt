import configuration.server.ktorServer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
fun main() {
    ktorServer().start(wait = true)
}