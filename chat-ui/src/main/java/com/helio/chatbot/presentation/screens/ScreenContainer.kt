package com.helio.chatbot.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import com.helio.chatbot.presentation.Modifiers

@Composable
fun ScreenContainer(content: @Composable ()->Unit) {
    Box(Modifiers.FillMaxSize) {
        content()
    }
}