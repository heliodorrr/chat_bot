package com.helio.chatbot.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.helio.chatbot.presentation.Modifiers
import com.helio.chatbot.presentation.R
import com.helio.chatbot.presentation.elements.VSpacer
import com.helio.chatbot.presentation.theme.Colors
import com.helio.chatbot.presentation.theme.Fonts
import com.helio.chatbot.presentation.theme.TextStyles
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Preview
@Composable
private fun SplashScreenPreview() = Box(modifier = Modifiers.FillMaxSize.background(Colors.Dark)){
    SplashScreen({})
}

private val WelcomeTextStyle = TextStyle(
    fontFamily = Fonts.Manrope,
    fontSize = 24.sp,
    color = Color.White,

)



@Composable
fun SplashScreen(
    navigateOut: ()->Unit
) {

    var isOut by remember { mutableStateOf(false) }

    Column(
        Modifiers.FillMaxSize
            .pointerInput(Unit) {
                detectTapGestures { isOut = true }
            }
    ) {



        LaunchedEffect(isOut) {
            if (!isOut) {
                delay((1.5).seconds)
                isOut = true
            } else {
                navigateOut()
            }
        }


        VSpacer(height = 140.dp)

        VSpacer(height = 80.dp)
        val modifier = remember {
            Modifier
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 14.dp)
        }
        Text(
            modifier = modifier,
            text = stringResource(id = R.string.splash_welcome),
            style = WelcomeTextStyle
        )
        VSpacer(height = 24.dp)
        Text(
            modifier = modifier,
            text = stringResource(id = R.string.splash_onboarding),
            style = TextStyles.MessageTextStyle,
            textAlign = TextAlign.Center
        )


    }

}









