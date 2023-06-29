package com.helio.chatbot.domain.mappers

import com.helio.chatbot.common.epochDay
import com.helio.chatbot.domain.model.ChatModel
import com.helio.chatbot.domain.model.DateDividerModel
import com.helio.chatbot.domain.model.MessageModel
import javax.inject.Inject
import kotlin.streams.toList

class DividerInserter @Inject constructor() {

    /**
     * Requires sorted list of data
     */
    suspend fun insertDividers(messageModels: List<MessageModel>): List<ChatModel> {
        messageModels.stream().parallel()
            .sorted { o1, o2 -> (o1.dateTime.epochDay - o2.dateTime.epochDay).toInt() }
            .toList()

        var prevDay = -1L
        val list = ArrayList<ChatModel>((messageModels.size * 1.5).toInt())

        messageModels.forEach { message ->
            val currentDay = message.dateTime.epochDay

            if (currentDay != prevDay) {
                prevDay = currentDay
                list.add(DateDividerModel(message.dateTime))
            }

            list.add(message)
        }

        return list

    }
}