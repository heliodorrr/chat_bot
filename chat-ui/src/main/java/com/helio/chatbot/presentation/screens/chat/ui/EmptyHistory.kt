package com.helio.chatbot.presentation.screens.chat.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.helio.chatbot.presentation.R
import com.helio.chatbot.presentation.elements.VSpacer


@Composable
internal fun EmptyHistory(modifier: Modifier) = Box(modifier.fillMaxSize()) {
    Column(
        Modifier
            .fillMaxWidth()
            .verticalScroll(
                rememberScrollState(),
                enabled = false
            )) {
        VSpacer(height = 90.dp)
        Image(
            modifier = Modifier
                .requiredWidth(200.dp)
                .wrapContentSize()
                .align(Alignment.CenterHorizontally)
            ,
            painter = painterResource(id = R.drawable.onboarding_content),
            contentDescription = ""
        )
    }
}