package com.helio.chatbot.data.repository

import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.helio.chatbot.common.di.MainComponentScope
import com.helio.chatbot.domain.repository.ChatRemoteRepository
import javax.inject.Inject

@MainComponentScope
class ChatRemoteRepositoryImpl @Inject constructor(
    private val openAI: OpenAI
): ChatRemoteRepository {

    companion object {
        private const val MODEL_ID = "gpt-3.5-turbo"
    }

    override suspend fun loadCompletion(
        history: List<ChatMessage>
    ) = openAI.chatCompletion(
        ChatCompletionRequest(
            model = ModelId(MODEL_ID),
            messages = history
        )
    )
}