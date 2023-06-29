package com.helio.chatbot.presentation.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object TextStyles {
    val MessageTextStyle = TextStyle(
        color = Colors.White,
        fontFamily = Fonts.Inter,
        fontWeight = FontWeight(400),
        fontSize = 18.sp
    )

    val PopupHeaderTextStyle = TextStyle(
        color = Colors.Dark,
        fontFamily = Fonts.Inter,
        fontWeight = FontWeight(500),
        fontSize = 18.sp
    )

    val PopupBodyTextStyle = TextStyle(
        color = Colors.Dark,
        fontFamily = Fonts.Manrope,
        fontWeight = FontWeight(400),
        fontSize = 18.sp
    )

    val ButtonTextStyle = TextStyle(
        color = Colors.White,
        fontFamily = Fonts.Manrope,
        fontWeight = FontWeight(600),
        fontSize = 18.sp
    )
}