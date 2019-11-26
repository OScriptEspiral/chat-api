package model

import java.time.Instant

data class Message(
    val _id: String?,
    val sentBy: String,
    val cardId: String,
    val sentTo: String,
    val content: String,
    val sendDateTime: Long = Instant.now().toEpochMilli())