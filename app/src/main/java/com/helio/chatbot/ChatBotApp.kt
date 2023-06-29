package com.helio.chatbot

import android.app.Application
import com.helio.chatbot.di.component.DaggerMainComponent
import com.helio.chatbot.presentation.di.component.ChatComponent
import com.helio.chatbot.presentation.di.component.ChatComponentProvider

class ChatBotApp: Application(), ChatComponentProvider {

    private val mainComponent by lazy(LazyThreadSafetyMode.NONE) {
        DaggerMainComponent.builder().setAppContext(this).build()
    }

    override val chatComponent: ChatComponent by lazy {
        mainComponent.presentationComponent().build()
    }


    override fun onCreate() {
        super.onCreate()

        run { chatComponent }


    }


}
