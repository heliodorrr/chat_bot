package com.helio.chatbot.presentation.di.component

import androidx.lifecycle.ViewModelProvider
import dagger.Subcomponent

@Subcomponent(modules = [ChatModule::class])
@ChatScope
interface ChatComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): ChatComponent
    }

    fun daggerViewModelFactory(): ViewModelProvider.Factory

}