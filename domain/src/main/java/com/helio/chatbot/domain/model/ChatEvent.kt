package com.helio.chatbot.domain.model

sealed class ChatEvent {

    object CompletionError: ChatEvent()
    object CompletionLoad: ChatEvent()
    class CompletionSuccess(val message: MessageModel): ChatEvent()

    class Batch(val messages: List<ChatModel>): ChatEvent()

    class UserMessage(val message: MessageModel): ChatEvent()
    class DividedUserMessage(
        val message: MessageModel,
        val divider: DateDividerModel
    ): ChatEvent()



}