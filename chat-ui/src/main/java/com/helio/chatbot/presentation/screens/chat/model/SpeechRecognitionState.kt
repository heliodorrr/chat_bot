package com.helio.chatbot.presentation.screens.chat.model

import com.helio.chatbot.domain.model.SpeechRecognitionEvent
import kotlinx.coroutines.flow.Flow

internal sealed class SpeechRecognitionState {
    class Start(val flow: Flow<SpeechRecognitionEvent>): SpeechRecognitionState()

    class Stop: SpeechRecognitionState()

}