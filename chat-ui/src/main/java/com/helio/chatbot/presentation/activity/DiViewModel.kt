package com.helio.chatbot.presentation.activity

import android.content.Context
import androidx.lifecycle.ViewModel
import com.helio.chatbot.presentation.di.component.ChatComponent
import com.helio.chatbot.presentation.di.component.ChatComponentProvider

internal class DiViewModel(): ViewModel() {

    lateinit var chatComponent: ChatComponent
    private set

    constructor(context: Context): this() {
        chatComponent =
            (context.applicationContext as ChatComponentProvider).chatComponent
    }
}