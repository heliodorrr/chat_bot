package com.helio.chatbot.presentation.elements.snackbar

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.helio.chatbot.presentation.theme.Colors
import com.helio.chatbot.presentation.theme.Fonts
import kotlinx.coroutines.delay
import rippleClickable

private val SnackbarHeight = 44.dp
@Composable
fun SliderSnackbar(
    modifier: Modifier,
    text: String,
    event: Any?
) {

    var showContent by remember { mutableStateOf(false) }
    var compositionWasEntered by remember { mutableStateOf(false) }

    val anim by animateFloatAsState(targetValue = if (showContent) 0f else -1f)

    Box(
        modifier
            .rippleClickable { showContent = false }
            .offset { IntOffset(0, (SnackbarHeight.roundToPx() * anim).toInt()) }
            .background(color = Colors.White)
            .fillMaxWidth()
            .height(SnackbarHeight)
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = text,
            fontFamily = Fonts.Inter,
            fontWeight = FontWeight(400),
            fontSize = 16.sp,
            color = Colors.Dark,
            textAlign = TextAlign.Center
        )
    }

    LaunchedEffect(event) {
        if (!compositionWasEntered) {
            compositionWasEntered = true
            return@LaunchedEffect
        }
        showContent = true
        delay(1500)
        showContent = false
    }

}
