package repository

import arrow.data.Reader
import arrow.data.ReaderApi.ask
import arrow.data.map
import com.mongodb.client.model.Filters
import configuration.server.RepositoryContext
import kotlinx.coroutines.runBlocking
import model.Message

fun save(message: Message): Reader<RepositoryContext, Message> =
    ask<RepositoryContext>().map { ctx ->
        runBlocking {
            message.also {
                ctx.COROUTINE_COLLECTION.insertOne(message)
            }
        }
    }

fun getByParticipantsAndCard(firstPersonId: String, secondPersonId: String, cardId: String) =
    ask<RepositoryContext>().map { ctx ->
        ctx.COROUTINE_COLLECTION.find(
            Filters.and(
                Filters.or(
                    Filters.eq("sentBy", firstPersonId),
                    Filters.eq("sentBy", secondPersonId),
                    Filters.eq("sentTo", firstPersonId),
                    Filters.eq("sentTo", secondPersonId)),
                Filters.eq("cardId", cardId)))
    }
