package com.helio.chatbot.domain.repository

import android.net.Uri
import com.helio.chatbot.domain.model.MessageModel

interface ExportPdfRepository {
    suspend fun produceAndExportPdf(uri: Uri, messages: List<MessageModel>)
}