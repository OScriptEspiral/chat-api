package configuration.exceptions

import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import exceptions.EntityNotFoundException
import exceptions.InsufficientParametersException
import io.ktor.application.call
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond

fun StatusPages.Configuration.exceptions() {

    exception<EntityNotFoundException> {
        HttpStatusCode.NotFound.let { status ->
            call.respond(status, ErrorResponse(status.value.toString(), status.description, it.message))
        }
    }

    exception<MissingKotlinParameterException> {
        HttpStatusCode.BadRequest.let { status ->
            call.respond(status, ErrorResponse(status.value.toString(), status.description, it.message ?: ""))
        }
    }

    exception<InsufficientParametersException> {
        HttpStatusCode.BadRequest.let { status ->
            call.respond(status, ErrorResponse(status.value.toString(), status.description, it.message))
        }
    }

    exception<Throwable> {
        HttpStatusCode.InternalServerError.let { status ->
            call.respond(status, ErrorResponse(status.value.toString(), status.description, it.message ?: ""))
        }
    }
}

class ErrorResponse(val statusCode: String, val status: String, val description: String)