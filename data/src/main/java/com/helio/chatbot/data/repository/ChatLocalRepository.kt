package com.helio.chatbot.data.repository


import com.helio.chatbot.common.di.MainComponentScope
import com.helio.chatbot.data.database.MessageDao
import com.helio.chatbot.data.model.MessageEntity
import com.helio.chatbot.domain.model.MessageModel
import com.helio.chatbot.domain.repository.ChatLocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@MainComponentScope
class ChatLocalRepositoryImpl @Inject constructor(
    private val messageDao: MessageDao
): ChatLocalRepository {
    override fun messagesFlow(): Flow<List<MessageModel>> {
        return messageDao.getHistory().map { list-> list.map { it.toMessageModel() } }
    }

    override suspend fun insertMessage(messageModel: MessageModel) {
        messageDao.insertMessage(MessageEntity.fromMessageModel(messageModel))
    }

    override suspend fun clear(): Result<Unit> = runCatching {
        messageDao.clearDatabase()
    }

}