package com.helio.chatbot.domain.repository

import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatMessage


interface ChatRemoteRepository {
    suspend fun loadCompletion(history: List<ChatMessage>): ChatCompletion
}