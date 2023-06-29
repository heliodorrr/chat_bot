package com.helio.chatbot.presentation.elements.chat

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.helio.chatbot.domain.model.MessageAuthor
import com.helio.chatbot.presentation.Modifiers
import com.helio.chatbot.presentation.elements.chat.model.ChatItem
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Preview
@Composable
private fun PreviewMessage() {

}

@Composable
fun Message(
    message: ChatItem.Message,
    animation: State<Float>,
    isFirst: Boolean = false,
    isLast: Boolean = false,
    onClipEvent: ()->Unit
) {

    val localScope = rememberCoroutineScope()
    val clipAnimation = remember { Animatable(1f) }
    val clipFloat by clipAnimation.asState()


    val cbm = LocalClipboardManager.current
    val density = LocalDensity.current


    val messageModifier = Modifier
        .pointerInput(message) {
            //TODO: extract such logic from here
            awaitPointerEventScope {
                var job: Job? = null
                outer@while (true) {
                    val event = awaitPointerEvent()
                    if (event.type == PointerEventType.Press) {
                        job = localScope.launch {
                            clipAnimation.animateTo(
                                targetValue = .97f,
                                animationSpec = tween(650)
                            )
                            cbm.setText(AnnotatedString(message.content))
                            onClipEvent()
                            clipAnimation.animateTo(1f)
                        }
                    }

                    var totalOffset = 0f
                    while (true) {

                        val innerEvent = awaitPointerEvent()
                        if (innerEvent.type == PointerEventType.Move) {
                            if (
                                innerEvent.changes.any {
                                    val threshhold = density.run { 5.dp.toPx() }
                                    totalOffset += it.positionChange().y
                                    totalOffset.absoluteValue > threshhold
                                }
                            ) {
                                job?.cancel()
                                localScope.launch {
                                    clipAnimation.animateTo(1f)
                                }
                                continue@outer
                            }
                        }

                        if (innerEvent.type == PointerEventType.Release) {
                            job?.cancel()
                            localScope.launch {
                                clipAnimation.animateTo(1f)
                            }
                            continue@outer
                        }
                    }
                }
            }

        }
        .drawWithContent {
            scale(
                scale = animation.value * clipFloat,
                pivot = Offset(
                    if (message.author == MessageAuthor.SYSTEM) { 0f }
                    else { size.width },
                    size.height/2f
                )
            ) {
                this@drawWithContent.drawContent()
            }
        }


    Box(messageModifier) {
        MessageContainer(
            messageAuthor = message.author,
            isFirst,
            isLast,
            messageContent = {
                Text(
                    text = message.content,
                    Modifier.width(IntrinsicSize.Max),
                    style = ChatConstants.Message.MessageTextStyle
                )
            },
            dateContent = {
                val rememberMessageModifier = remember(message.author) {
                    Modifier
                        .run {
                            when(message.author) {
                                MessageAuthor.ME -> align(Alignment.End)
                                MessageAuthor.SYSTEM -> align(Alignment.Start)
                            }
                        }
                        .padding(horizontal = 8.dp)
                        .width(IntrinsicSize.Max)
                }

                Text(
                    text = message.dateTime,
                    modifier = rememberMessageModifier,
                    style = ChatConstants.Message.DateTextStyle,
                )
            }
        )
    }

}


@Composable
internal fun MessageContainer(
    messageAuthor: MessageAuthor,
    isFirst: Boolean = false,
    isLast: Boolean = false,
    dateContent: @Composable (ColumnScope.()->Unit)? = null,
    messageContent: @Composable BoxScope.()->Unit,
) = Box(Modifiers.FillMaxWidth) {

    val boxModifier = remember(messageAuthor) {
        Modifier
            .background(messageAuthor.messageColor, messageAuthor.messageShape)
            .padding(ChatConstants.Message.MessagePadding)
    }

    val columnModifier = remember(messageAuthor) {
        ChatConstants.Message
            .applyVerticalPaddings(Modifier, isFirst, isLast)
            .run {
                when (messageAuthor) {
                    MessageAuthor.ME -> padding(start = ChatConstants.Message.EdgeMargin)
                        .align(Alignment.CenterEnd)

                    MessageAuthor.SYSTEM -> padding(end = ChatConstants.Message.EdgeMargin)
                        .align(Alignment.CenterStart)
                }
            }
    }

    Column(columnModifier.width(IntrinsicSize.Min)) {
        Box(boxModifier.fillMaxWidth()) {
            messageContent()
        }
        dateContent?.invoke(this)
    }

}

