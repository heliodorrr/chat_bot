package com.helio.chatbot.presentation.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.helio.chatbot.presentation.R

object Fonts {
    val Inter = FontFamily(
        Font(resId = R.font.inter_400, weight = FontWeight(400)),
        Font(resId = R.font.inter_500, weight = FontWeight(500))
    )
    val Manrope = FontFamily(
        Font(resId = R.font.manrope_600, weight = FontWeight(600)),
        Font(resId = R.font.manrope_400, weight = FontWeight(400))
    )
}