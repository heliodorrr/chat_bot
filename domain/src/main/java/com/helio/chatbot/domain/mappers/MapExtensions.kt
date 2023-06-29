package com.helio.chatbot.domain.mappers

import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.helio.chatbot.domain.model.MessageModel
import com.helio.chatbot.domain.model.MessageAuthor
import java.time.OffsetDateTime


fun ChatRole.toMessageType(): MessageAuthor = when(this) {
    ChatRole.User -> MessageAuthor.ME
    else -> MessageAuthor.SYSTEM
}

fun ChatMessage.toMessageWithCurrentDateTime(): MessageModel {
    return MessageModel(
        content = content,
        dateTime = OffsetDateTime.now(),
        author = role.toMessageType()
    )
}

fun MessageModel.toChatMessage(): ChatMessage {
    return ChatMessage(
        role = when(author) {
            MessageAuthor.ME -> ChatRole.User
            MessageAuthor.SYSTEM -> ChatRole.System
        },
        content = content,
    )
}

fun ChatCompletion.toChatMessage(): ChatMessage? {
    return choices.firstOrNull()?.message
}


