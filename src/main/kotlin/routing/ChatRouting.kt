package routing

import exceptions.InsufficientParametersException
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respondTextWriter
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.broadcast
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.reactive.collect
import model.Message
import org.litote.kmongo.json
import service.getByParticipantsAndCard
import service.save

@FlowPreview
@ExperimentalCoroutinesApi
fun Application.chatRoutes() {
    val channel = produce<Message> {
        while (true) {} //polling to keep channel open
    }.broadcast()

    routing {
        get {
            val firstPersonId = call.request.queryParameters["firstPersonId"] ?: throw InsufficientParametersException()
            val secondPersonId = call.request.queryParameters["secondPersonId"] ?: throw InsufficientParametersException()
            val cardId = call.request.queryParameters["cardId"] ?: throw InsufficientParametersException()

            call.respondTextWriter(ContentType.Text.Plain) {
                getByParticipantsAndCard(firstPersonId, secondPersonId, cardId).collect { studentPerformance ->
                    this.write("data: ${studentPerformance.json}\n")
                    this.flush()
                }
            }
        }

        get("/subscribe") {
            val events = channel.openSubscription()
            val sentTo = call.request.queryParameters["sentTo"] ?: throw InsufficientParametersException()
            try {
                call.respondTextWriter(contentType = ContentType.Text.EventStream) {
                    events.consumeAsFlow().filter {
                        it.sentTo == sentTo
                    }.collect { performance ->
                        write("data: ${performance.json}\n")
                        flush()
                    }
                }
            } finally {
                events.cancel()
            }
        }

        post {
            call.response.let { response ->
                save(call.receive()).let { message ->
                    response.headers.append("Location", "/${message._id}", true)
                    channel.send(message)
                }
                response.status(HttpStatusCode.Created)
            }
        }
    }
}