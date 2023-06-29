package com.helio.chatbot.domain.logic.usecase

import com.helio.chatbot.domain.repository.ChatLocalRepository
import javax.inject.Inject

class ClearLocalStorageUseCase @Inject constructor(
    private val localRepository: ChatLocalRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return localRepository.clear()
    }
}