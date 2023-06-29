package com.helio.chatbot.presentation.elements.snackbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

@Preview
@Composable
private fun PreviewCustomSB() {
    val events by flow {
        repeat(10000) { delay(3000); emit(it) }
    }.collectAsState(initial = -1)

   // SliderSnackbar(text = "asdasd", event = events)



}

