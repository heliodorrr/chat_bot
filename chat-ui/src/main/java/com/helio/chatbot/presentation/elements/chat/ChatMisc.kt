package com.helio.chatbot.presentation.elements.chat

import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.helio.chatbot.domain.model.MessageAuthor
import com.helio.chatbot.presentation.elements.ElementsSharedConstants
import com.helio.chatbot.presentation.theme.Colors
import com.helio.chatbot.presentation.theme.TextStyles

internal object ChatConstants {

    object DateDivider {
        val MyMessage = Colors.DarkGray
        val Shape = RoundedCornerShape(10.dp)
    }

    object Message {
        val MyMessageColor = Colors.MidGray
        val SystemMessageColor = Colors.DarkGray

        val MessageTextStyle = TextStyles.MessageTextStyle
        val DateTextStyle = TextStyles.MessageTextStyle.copy(fontSize = 14.sp)

        val Round = 10.dp
        val EdgeMargin = 24.dp
        val MessagePadding = 16.dp

        val InterMessageVPadding = 12.dp
        private val TopMessagePadding = 6.dp
        private val BottomMessagePadding =
            InterMessageVPadding + ElementsSharedConstants.InputFieldBackgroundRound


        fun applyVerticalPaddings(
            modifier: Modifier, isFirst: Boolean, isLast: Boolean
        ): Modifier {

            return modifier
                .padding(
                    top = if (isFirst) TopMessagePadding else InterMessageVPadding,
                    bottom = if (isLast) BottomMessagePadding else InterMessageVPadding,
                )

        }


    }


    object Placeholder {
        val HorizontalPadding = 12.dp

        const val DotCount = 3
        const val IntervalCount = DotCount - 1

        val DotColor = Color.White
        val BaseRadius = 2.5.dp
        val AdditionalRadius = (1).dp

        private val DotMargin = 40.dp
        val Width = DotMargin * IntervalCount


        val AnimationSpec = InfiniteRepeatableSpec<Float>(
            animation = tween(750),
            repeatMode = RepeatMode.Restart
        )

        val ZeroAlphaModifier = Modifier.alpha(0f)
    }

}

internal val MessageAuthor.messageColor get() = when(this) {
    MessageAuthor.ME -> ChatConstants.Message.MyMessageColor
    MessageAuthor.SYSTEM -> ChatConstants.Message.SystemMessageColor
}

internal val MessageAuthor.messageShape: Shape get() = when(this) {
    MessageAuthor.ME -> RoundedCornerShape(
        topStart = ChatConstants.Message.Round, bottomStart = ChatConstants.Message.Round
    )
    MessageAuthor.SYSTEM -> RoundedCornerShape(
        bottomEnd = ChatConstants.Message.Round, topEnd = ChatConstants.Message.Round
    )
}