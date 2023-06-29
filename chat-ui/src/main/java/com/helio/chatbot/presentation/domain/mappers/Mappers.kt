package com.helio.chatbot.presentation.domain.mappers


import com.helio.chatbot.domain.model.ChatModel
import com.helio.chatbot.domain.model.DateDividerModel
import com.helio.chatbot.domain.model.MessageModel
import com.helio.chatbot.presentation.elements.chat.model.ChatItem
import java.time.OffsetDateTime
import java.time.format.TextStyle
import java.util.Locale

private fun OffsetDateTime.formatDate(): String {
    val local = toLocalDateTime()
    return buildString {

        append(local.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault()))
        append(", "); append(local.dayOfMonth); append(" ")
        append(local.month.getDisplayName(TextStyle.FULL, Locale.getDefault())); append(" ")
        append(local.year.toString())
    }
}

private fun OffsetDateTime.formatTime(): String {
    val local = toLocalDateTime()
    return buildString {
        append(local.hour); append(":")
        if (local.minute < 10) {
            append(0)
        }
        append(local.minute)
    }
}

fun ChatModel.toChatItem() = when(this) {
    is DateDividerModel -> ChatItem.DateDivider(dateTime.formatDate())
    is MessageModel -> ChatItem.Message(content, dateTime.formatTime(), author)
}

fun List<ChatModel>.toChatItems(): List<ChatItem> {
    return map(ChatModel::toChatItem)
}