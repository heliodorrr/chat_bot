package com.helio.chatbot.presentation.elements.chat.model

import com.helio.chatbot.domain.model.MessageAuthor

sealed class ChatItem {


    class Message(
        val content: String,
        val dateTime: String,
        val author: MessageAuthor
    ): ChatItem()

    class DateDivider(
        val date: String
    ): ChatItem()

    object LoadingMessage: ChatItem()
}
