package com.helio.chatbot.domain.repository

import com.helio.chatbot.domain.model.MessageModel
import kotlinx.coroutines.flow.Flow

interface ChatLocalRepository {
    fun messagesFlow(): Flow<List<MessageModel>>

    suspend fun insertMessage(messageModel: MessageModel)

    suspend fun clear(): Result<Unit>
}