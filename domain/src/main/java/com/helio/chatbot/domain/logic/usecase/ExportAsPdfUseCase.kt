package com.helio.chatbot.domain.logic.usecase

import android.net.Uri
import com.helio.chatbot.common.AsyncState
import com.helio.chatbot.common.flowFromCoroutine
import com.helio.chatbot.domain.repository.ChatLocalRepository
import com.helio.chatbot.domain.repository.ExportPdfRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ExportAsPdfUseCase @Inject constructor(
    private val localRepository: ChatLocalRepository,
    private val repository: ExportPdfRepository
) {

    operator fun invoke(
        uri: Uri
    ): Flow<AsyncState<Unit>> {
        return flowFromCoroutine {
            val messages = localRepository
                .messagesFlow()
                .first()
            repository.produceAndExportPdf(uri, messages)

        }
    }

}






