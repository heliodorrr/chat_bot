package com.helio.chatbot.presentation.elements.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.helio.chatbot.presentation.R
import com.helio.chatbot.presentation.theme.Colors

@Preview
@Composable
private fun ChatHeaderPreview() {

}

private val IconVMargin = 4.dp
private val IconHMargin = 8.dp
private val IconSize = 30.dp

private val HeaderModifier = Modifier
    .background(color = Colors.Dark)
    .wrapContentHeight()
    .fillMaxWidth()

@Composable
fun ChatHeader(
    onClearHistoryClick: ()->Unit,
    modifier: Modifier
) = ConstraintLayout(HeaderModifier then modifier) {

    Icon(
        modifier = Modifier
            .size(IconSize)
            .clickable { onClearHistoryClick() }
            .constrainAs(createRef()) {
                top.linkTo(parent.top, IconVMargin)
                bottom.linkTo(parent.bottom, IconVMargin)
                end.linkTo(parent.end, IconHMargin)
            },
        painter = painterResource(id = R.drawable.icon_clean_history),
        contentDescription = "",
        tint = Color.Unspecified
    )

}