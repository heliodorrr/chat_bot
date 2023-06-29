package com.helio.chatbot.domain.logic

import com.aallam.openai.api.chat.ChatMessage
import com.helio.chatbot.common.epochDay
import com.helio.chatbot.domain.mappers.DividerInserter
import com.helio.chatbot.domain.model.ChatEvent
import com.helio.chatbot.domain.model.MessageModel
import com.helio.chatbot.domain.mappers.toChatMessage
import com.helio.chatbot.domain.mappers.toMessageWithCurrentDateTime
import com.helio.chatbot.domain.model.DateDividerModel
import com.helio.chatbot.domain.repository.ChatLocalRepository
import com.helio.chatbot.domain.repository.ChatRemoteRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

//TODO: write unit testing
class ChatRoomService @Inject constructor(
    private val localRepository: ChatLocalRepository,
    private val remoteRepository: ChatRemoteRepository,
    private val dividerInserter: DividerInserter
) {
    private val _computationChannel = Channel<suspend (()->ChatEvent?)>(Channel.UNLIMITED)

    private var latestMessage: MessageModel? = null
    private val _chatMessagesCache = mutableListOf<ChatMessage>()

    /**
     * @return Flow derived from a channel. It is a hot flow, without no replay, any collected value
     * is consumed.
     */
    val actions = _computationChannel
        .consumeAsFlow()
        .map { it.invoke() }
        .filterNotNull()

    init {
        _computationChannel.trySendBlocking {
            localRepository.messagesFlow().first().let {  messages ->
                _chatMessagesCache.addAll(messages.map(MessageModel::toChatMessage))
                latestMessage = messages.lastOrNull()
                ChatEvent.Batch(dividerInserter.insertDividers(messages))
            }
        }
    }

    /**
     * Sends a completion request. Output is expected at events
     */
    fun loadCompletion(userMessage: MessageModel) {
        val userChatMessage = userMessage.toChatMessage()

        _computationChannel.trySendBlocking {
            val lastMsgEpochDay = latestMessage?.dateTime?.epochDay
            latestMessage = userMessage
            if (lastMsgEpochDay == null || lastMsgEpochDay < userMessage.dateTime.epochDay) {
                ChatEvent.DividedUserMessage(
                    message = userMessage,
                    divider = DateDividerModel(userMessage.dateTime)
                )
            } else {
                ChatEvent.UserMessage(userMessage)
            }
        }
        _computationChannel.trySendBlocking { ChatEvent.CompletionLoad }
        _computationChannel.trySendBlocking {
            writeMessage(userMessage)
            addToCache(userChatMessage)
            val completionResult = runCatching {

                remoteRepository.loadCompletion(_chatMessagesCache)
                    .toChatMessage()
            }

            val completion = completionResult.getOrNull()
            if (completion != null) {
                val completionMessageModel = completion.toMessageWithCurrentDateTime()
                //saving all related to completion
                addToCache(completion)
                writeMessage(completionMessageModel)

                latestMessage = completionMessageModel

                ChatEvent.CompletionSuccess(completionMessageModel)
            } else {
                ChatEvent.CompletionError
            }
        }
    }

    private suspend fun writeMessage(messageModel: MessageModel) {
        localRepository.insertMessage(messageModel)
    }

    private fun addToCache(chatMessage: ChatMessage) {
        _chatMessagesCache.add(chatMessage)
    }



}