package com.helio.chatbot.domain.model

import java.time.OffsetDateTime

sealed class ChatModel

data class MessageModel(
    val content: String,
    val dateTime: OffsetDateTime,
    val author: MessageAuthor
): ChatModel() {

}

data class DateDividerModel(
    val dateTime: OffsetDateTime,
): ChatModel()