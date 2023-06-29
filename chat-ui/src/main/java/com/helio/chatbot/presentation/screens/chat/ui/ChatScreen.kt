package com.helio.chatbot.presentation.screens.chat.ui

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.helio.chatbot.domain.model.SpeechRecognitionEvent
import com.helio.chatbot.presentation.Modifiers
import com.helio.chatbot.presentation.elements.ClearHistoryDialog
import com.helio.chatbot.presentation.elements.ElementsSharedConstants
import com.helio.chatbot.presentation.elements.SomeErrorDialog
import com.helio.chatbot.presentation.elements.chat.ChatHeader
import com.helio.chatbot.presentation.elements.chat.MessageWall
import com.helio.chatbot.presentation.elements.chat.model.ChatItem
import com.helio.chatbot.presentation.elements.inputfield.InputField
import com.helio.chatbot.presentation.elements.snackbar.SliderSnackbar
import com.helio.chatbot.presentation.screens.ScreenContainer
import com.helio.chatbot.presentation.screens.chat.model.ChatViewModel
import kotlinx.coroutines.flow.Flow

@Preview
@Composable
private fun ChatScreenPreview() = ScreenContainer {

}

@Composable
fun ChatScreen(viewModel: ChatViewModel) {
    val messages = remember(viewModel.chatItems) { viewModel.chatItems.asReversed() }

    var showErrorDialog by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        viewModel.events.collect {
            showErrorDialog = true
        }
    }

    if (showErrorDialog) {
        SomeErrorDialog { showErrorDialog = false }
    }

    
    ChatScreenInternal(
        messages = messages,
        onSend = viewModel::sendMessage,
        onClearHistoryRequest = viewModel::clearHistory,
        onSpeechRecognitionButton = viewModel::onSpeechRecognition,
        speechRecognitionFlow = viewModel.speechRecognitionFlow,
        onPdfExport = viewModel::exportAsPdf
    )
}

@Composable
private fun ChatScreenInternal(
    messages: List<ChatItem>,
    onSend: (String)->Unit,
    onClearHistoryRequest: ()->Unit,
    onSpeechRecognitionButton: ()->Unit,
    speechRecognitionFlow: Flow<SpeechRecognitionEvent>,
    onPdfExport: (Uri)->Unit
) = Box(Modifiers.FillMaxSize) {

    ConstraintLayout(Modifiers.FillMaxSize.clip(RectangleShape)) {
        val inputRef = createRef()
        val headerRef = createRef()
        val snackbarRef = createRef()

        var clipEvent by remember { mutableStateOf<Any?>(null) }

        var clearHistoryDialog by remember { mutableStateOf(false) }
        if (clearHistoryDialog) {
            ClearHistoryDialog(
                onDismiss = { clearHistoryDialog = false },
                onConfirm = onClearHistoryRequest
            )
        }


        val isEmpty = messages.isEmpty()


        val constraintModifier = remember(isEmpty) {
            Modifier
                .constrainAs(createRef()) {
                    height = Dimension.fillToConstraints
                    if (isEmpty) {
                        top.linkTo(parent.top)
                    } else {
                        top.linkTo(headerRef.bottom)
                    }

                    start.linkTo(parent.start)
                    bottom.linkTo(inputRef.top, - ElementsSharedConstants.InputFieldBackgroundRound)
                }
        }

        if (isEmpty) {
            EmptyHistory(modifier = constraintModifier)
        } else {
            MessageWall(
                modifier = constraintModifier,
                messages = messages,
                onClipEvent = { clipEvent = Any() }
            )
        }

        if (!isEmpty) {
            SliderSnackbar(
                modifier = Modifier
                    .constrainAs(snackbarRef) {
                        top.linkTo(headerRef.bottom)
                    },
                text = "Text has been copied",
                event = clipEvent
            )

            ChatHeader(
                onClearHistoryClick = { clearHistoryDialog = true },
                modifier = Modifier.constrainAs(headerRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
            )
        }



        InputField(
            modifier = Modifier
                .constrainAs(inputRef) {
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                },
            onSend = onSend,
            enabled = messages.firstOrNull() != ChatItem.LoadingMessage,
            onSpeechRecognitionButton = onSpeechRecognitionButton,
            speechRecognitionFlow = speechRecognitionFlow,
            pdfExportEnabled = !isEmpty,
            onExportAsPdf = onPdfExport,
            pdfExportState = null
        )
    }
}



