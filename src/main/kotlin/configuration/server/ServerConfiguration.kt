package configuration.server

import configuration.exceptions.exceptions
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.jackson.jackson
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import routing.chatRoutes

@ExperimentalCoroutinesApi
@FlowPreview
fun ktorServer() = embeddedServer(Netty, port = 8080) {
    installExceptions()
    installRequestPayloadDeserializer()
    chatRoutes()
}

fun Application.installRequestPayloadDeserializer() = install(ContentNegotiation) {
    jackson {  }
}

fun Application.installExceptions() = install(StatusPages) {
    exceptions()
}