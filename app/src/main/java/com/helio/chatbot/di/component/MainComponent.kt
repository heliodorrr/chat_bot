package com.helio.chatbot.di.component

import android.content.Context
import com.helio.chatbot.common.di.AppContext
import com.helio.chatbot.common.di.MainComponentScope
import com.helio.chatbot.data.di.DataModule
import com.helio.chatbot.presentation.di.component.ChatComponent
import dagger.BindsInstance
import dagger.Component


@Component(
    modules = [MainModule::class, DataModule::class]
)
@MainComponentScope
interface MainComponent {

    @Component.Builder
    interface Builder {
        fun setAppContext(@AppContext @BindsInstance context: Context): Builder
        fun build(): MainComponent
    }

    fun presentationComponent(): ChatComponent.Builder

}