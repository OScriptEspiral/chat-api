package service

import arrow.core.fix
import configuration.server.RepositoryContext
import io.reactivex.Flowable
import model.Message

fun save(message: Message) =
    repository.save(message).run(RepositoryContext).fix().extract()

fun getByParticipantsAndCard(firstPersonId: String, secondPersonId: String, cardId: String) =
    Flowable.fromPublisher(
        repository.getByParticipantsAndCard(firstPersonId, secondPersonId, cardId)
            .run(RepositoryContext).fix().extract().publisher)
