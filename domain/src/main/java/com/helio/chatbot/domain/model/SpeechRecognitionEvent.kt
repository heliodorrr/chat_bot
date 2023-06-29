package com.helio.chatbot.domain.model

sealed class SpeechRecognitionEvent {
    object Started: SpeechRecognitionEvent()

    object ActiveMike: SpeechRecognitionEvent()

    sealed class Terminal: SpeechRecognitionEvent()

    object NotStarted: Terminal()

    class Text(val text: String): Terminal()
    object End: Terminal()

}
