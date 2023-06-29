package com.helio.chatbot.presentation.screens.chat.model


import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.helio.chatbot.common.AsyncState
import com.helio.chatbot.domain.logic.ChatRoomService
import com.helio.chatbot.domain.logic.usecase.ClearLocalStorageUseCase
import com.helio.chatbot.domain.logic.usecase.ExportAsPdfUseCase
import com.helio.chatbot.domain.logic.usecase.SpeechToTextFlowUseCase
import com.helio.chatbot.domain.model.ChatEvent
import com.helio.chatbot.domain.model.MessageAuthor
import com.helio.chatbot.domain.model.MessageModel
import com.helio.chatbot.domain.model.SpeechRecognitionEvent
import com.helio.chatbot.presentation.domain.mappers.toChatItem
import com.helio.chatbot.presentation.domain.mappers.toChatItems
import com.helio.chatbot.presentation.elements.chat.model.ChatItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.OffsetDateTime
import javax.inject.Inject
import javax.inject.Provider

class ChatViewModel @Inject constructor(
    private val chatRoomServiceProvider: Provider<ChatRoomService>,
    private val clearLocalStorageUseCase: ClearLocalStorageUseCase,
    private val speechToTextFlowUseCase: SpeechToTextFlowUseCase,
    private val sharePdfUseCase: ExportAsPdfUseCase
): ViewModel() {

    val speechRecognitionFlow = speechToTextFlowUseCase.speechRecognitionEventFlow()
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Eagerly, SpeechRecognitionEvent.NotStarted)


    private val chatRoomServiceFlow = MutableStateFlow<ChatRoomService?>(
        chatRoomServiceProvider.get()
    )

    private val pdfExportState = MutableStateFlow<AsyncState<Unit>?>(null)

    private val _events = Channel<ChatScreenEvent>(Channel.Factory.UNLIMITED)
    val events = _events
        .consumeAsFlow()


    val chatItems = mutableStateListOf<ChatItem>()

    init
    {
        viewModelScope.launch {
            chatRoomServiceFlow
                .filterNotNull()
                .collectLatest { service-> collectEvents(service.actions) }
        }
    }

    fun onSpeechRecognition() {
        if (speechRecognitionFlow.value !is SpeechRecognitionEvent.Terminal) {
            speechToTextFlowUseCase.stop()
        } else {
            speechToTextFlowUseCase.start()
        }

    }

    fun exportAsPdf(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            pdfExportState.emitAll(sharePdfUseCase.invoke(uri))
            pdfExportState.emit(null)
        }
    }

    private suspend fun collectEvents(dataEvents: Flow<ChatEvent>) = dataEvents
        .flowOn(Dispatchers.IO)
        .collect { event ->
            when(event) {
                is ChatEvent.CompletionError -> {
                    _events.trySendBlocking(ChatScreenEvent.COMPLETION_ERROR)
                    chatItems.removeLast()
                }
                is ChatEvent.UserMessage -> {
                    chatItems.add(event.message.toChatItem())
                }
                is ChatEvent.CompletionSuccess -> {
                    chatItems.removeLast()
                    chatItems.add(event.message.toChatItem())
                }
                is ChatEvent.DividedUserMessage -> {
                    chatItems.add(event.divider.toChatItem())
                    chatItems.add(event.message.toChatItem())
                }
                is ChatEvent.CompletionLoad -> {
                    chatItems.add(ChatItem.LoadingMessage)
                }
                is ChatEvent.Batch -> {
                    chatItems.addAll(event.messages.toChatItems())
                }
            }
        }

    fun clearHistory() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                clearLocalStorageUseCase()
            }
                .onSuccess {
                    chatRoomServiceFlow.value = null
                    chatItems.clear()
                    chatRoomServiceFlow.value = chatRoomServiceProvider.get()
                }
                .onFailure {

                }
        }
    }

    fun sendMessage(content: String) = chatRoomServiceFlow.value?.loadCompletion(
        MessageModel(
            content = content,
            dateTime = OffsetDateTime.now(),
            author = MessageAuthor.ME
        )
    )

}