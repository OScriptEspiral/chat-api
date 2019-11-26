package configuration.server

import configuration.database.mongoConnection
import model.Message
import org.litote.kmongo.coroutine.CoroutineCollection

object RepositoryContext {
    val COROUTINE_COLLECTION: CoroutineCollection<Message> = mongoConnection()
}