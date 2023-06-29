package com.helio.chatbot.presentation.elements.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.helio.chatbot.presentation.elements.chat.model.ChatItem
import com.helio.chatbot.presentation.theme.TextStyles

@Composable
@Preview
fun PreviewDateDivider() {

}

private val DateDividerModifier = Modifier
    .padding(
        horizontal = ChatConstants.Message.MessagePadding,
        vertical = ChatConstants.Message.InterMessageVPadding
    )
    .height(40.dp)
    .fillMaxWidth()

private val TextModifier = Modifier
    .background(ChatConstants.DateDivider.MyMessage, ChatConstants.DateDivider.Shape)
    .padding(horizontal = 16.dp, vertical = 6.dp)

private val DateDividerTextStyle = TextStyles.MessageTextStyle.copy(fontSize = 14.sp)

@Composable
fun DateDivider(
    item: ChatItem.DateDivider
) = Box(DateDividerModifier) {
    Text(
        text = item.date,
        modifier = TextModifier.align(Alignment.Center),
        style = DateDividerTextStyle
    )
}

