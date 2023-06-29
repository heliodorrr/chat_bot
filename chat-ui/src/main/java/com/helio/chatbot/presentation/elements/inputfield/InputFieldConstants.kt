package com.helio.chatbot.presentation.elements.inputfield

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.helio.chatbot.presentation.elements.ElementsSharedConstants
import com.helio.chatbot.presentation.theme.TextStyles

object InputFieldConstants {

    const val PdfMime = "application/pdf"

    val ExportAsColor = Color(0xFFCDCED4)

    private val BackgroundColor = Color(0xFF676874)
    private val BackgroundShape = RectangleShape//RoundedCornerShape(topStart = BackgroundRound, topEnd = BackgroundRound)
    val BgPadding = 14.dp
    val BgModifier = Modifier
        .background(BackgroundColor, BackgroundShape)
        .navigationBarsPadding()
        .padding(BgPadding)

    private val ForegroundColor = Color(0xFFE9E9E9)
    private val ForegroundRound = 10.dp
    private val ForegroundShape = RoundedCornerShape(ForegroundRound)
    private val FgPadding = 14.dp
    val FgModifier = Modifier
        .background(
            ForegroundColor, ForegroundShape
        )
        .padding(FgPadding)

    private val TextColor = Color(0xFF797979)
    val InputFieldTextStyle = TextStyles.MessageTextStyle.copy(color = TextColor)


}