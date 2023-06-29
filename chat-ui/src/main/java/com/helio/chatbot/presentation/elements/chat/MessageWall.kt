package com.helio.chatbot.presentation.elements.chat

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.helio.chatbot.presentation.elements.chat.model.ChatItem
import com.helio.chatbot.presentation.screens.chat.ui.ChatScreenConstants
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessageWall(
    modifier: Modifier,
    messages: List<ChatItem>,
    onClipEvent: ()->Unit
) {

    val state = rememberLazyListState()
    val animationState: MutableMap<ChatItem, State<Float>> = remember { ConcurrentHashMap() }

    val scope = rememberCoroutineScope()
    fun requestAnimation(chatItem: ChatItem): State<Float> {
        val animation = animationState[chatItem]
        return if (animation == null) {
            val animatable = Animatable(0f)
            scope.launch { animatable.animateTo(1f, ChatScreenConstants.AnimationSpec) }
            animatable.asState().also { animationState[chatItem] = it }
        } else {
            animation
        }
    }


    LazyColumn(
        reverseLayout = true,
        state = state,
        modifier = modifier
    ) {

        itemsIndexed(
            messages,
            contentType = { index, item -> item::class }
        ) { index, chatItem ->
            val animation = requestAnimation(chatItem)

            Box(Modifier.animateItemPlacement()) {
                when (chatItem) {
                    is ChatItem.Message -> {
                            Message(
                                message = chatItem,
                                animation = animation,
                                onClipEvent = onClipEvent,
                                isFirst = index == messages.lastIndex,
                                isLast = index == 0,
                            )
                    }
                    is ChatItem.LoadingMessage -> MessagePlaceholder()
                    is ChatItem.DateDivider -> DateDivider(chatItem)
                }
            }
        }
    }
}