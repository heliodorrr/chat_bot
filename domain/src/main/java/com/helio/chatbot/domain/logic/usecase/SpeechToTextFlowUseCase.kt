package com.helio.chatbot.domain.logic.usecase

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import com.helio.chatbot.common.di.AppContext
import com.helio.chatbot.domain.model.SpeechRecognitionEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SpeechToTextFlowUseCase @Inject constructor(
    @AppContext private val context: Context
) {

    companion object {
        private const val COMMAND_START = 0
        private const val COMMAND_STOP = 1
    }

    private val commandChannel = Channel<Int>(Channel.UNLIMITED)

    fun speechRecognitionEventFlow() = callbackFlow<SpeechRecognitionEvent> {
        var previousJob: Job? = null

        while (true) {
            when(commandChannel.receive()) {
                COMMAND_START -> {
                    previousJob?.let {
                        it.cancel();
                        trySend(SpeechRecognitionEvent.End)

                    }
                    previousJob = launch {
                        generateCompletionFlow().collect { trySend(it) }
                    }
                }
                COMMAND_STOP -> {
                    previousJob?.cancel()
                    trySend(SpeechRecognitionEvent.End)
                }
            }
        }
    }

    fun start() {
        commandChannel.trySend(COMMAND_START)
    }

    fun stop() {
        commandChannel.trySend(COMMAND_STOP)
    }

    private fun generateCompletionFlow(): Flow<SpeechRecognitionEvent> {
        return callbackFlow {
            trySend(SpeechRecognitionEvent.Started)

            val speechRecognizer = withContext(Dispatchers.Main) {
                val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
                val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
               // speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

                val listener = object: RecognitionListener {
                    override fun onReadyForSpeech(params: Bundle?) {}
                    override fun onBeginningOfSpeech() { trySend(SpeechRecognitionEvent.ActiveMike) }
                    override fun onRmsChanged(rmsdB: Float) {}
                    override fun onBufferReceived(buffer: ByteArray?) {}
                    override fun onEndOfSpeech() {}
                    override fun onError(error: Int) { trySend(SpeechRecognitionEvent.End) }
                    override fun onResults(results: Bundle?) {
                        val result = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        result?.firstOrNull()?.let { trySend(SpeechRecognitionEvent.Text(it)) }
                    }
                    override fun onPartialResults(partialResults: Bundle?) {}
                    override fun onEvent(eventType: Int, params: Bundle?) {}
                }

                speechRecognizer.setRecognitionListener(listener)
                speechRecognizer.startListening(speechRecognizerIntent)

                speechRecognizer
            }
            awaitClose { speechRecognizer.destroy() }
        }
    }



}