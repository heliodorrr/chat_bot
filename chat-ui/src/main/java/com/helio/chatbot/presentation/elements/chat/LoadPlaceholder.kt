package com.helio.chatbot.presentation.elements.chat

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import com.helio.chatbot.domain.model.MessageAuthor
import kotlin.math.absoluteValue


@Composable
@Preview
private fun LoadingMessagePreview() {
    MessagePlaceholder()
}


@Composable
fun MessagePlaceholder()
= Box(
    Modifier.height(IntrinsicSize.Min)
) {
    MessageContainer(
        messageAuthor = MessageAuthor.SYSTEM,
        isFirst = false,
        isLast = true
    ) {

        Text(text = "", modifier = ChatConstants.Placeholder.ZeroAlphaModifier)

        val fraction by rememberInfiniteTransition("").animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = ChatConstants.Placeholder.AnimationSpec,
            label = ""
        )

        Canvas(
            Modifier
                .fillMaxHeight()
                .width(ChatConstants.Placeholder.Width)
                .padding(horizontal = ChatConstants.Placeholder.HorizontalPadding)
                .align(Alignment.Center)
        ) {


            val baseRad = ChatConstants.Placeholder.BaseRadius.toPx()
            val addRad = ChatConstants.Placeholder.AdditionalRadius.toPx()
            val interval = size.width/ChatConstants.Placeholder.IntervalCount
            val y = size.height/2

            var x = 0f
            var next = interval
            val pos = fraction * size.width

            repeat(ChatConstants.Placeholder.DotCount) {

                if (pos in x-interval..x+interval) {
                    drawCircle(
                        color = ChatConstants.Placeholder.DotColor,
                        center = Offset(x, y),
                        radius = ((1f-((x-pos).absoluteValue)/interval) * addRad) + baseRad
                    )
                } else {
                    drawCircle(
                        color = ChatConstants.Placeholder.DotColor,
                        center = Offset(x, y),
                        radius = baseRad
                    )
                }

                x = next
                next += interval
            }

        }

    }

}