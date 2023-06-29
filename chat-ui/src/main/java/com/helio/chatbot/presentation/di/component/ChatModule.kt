package com.helio.chatbot.presentation.di.component

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.helio.chatbot.presentation.di.DaggerVMProviderFactory
import com.helio.chatbot.presentation.di.ViewModelKey
import com.helio.chatbot.presentation.screens.chat.model.ChatViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ChatModule {
    @Binds
    fun bindDVMPFactory(factory: DaggerVMProviderFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ChatViewModel::class)
    fun chatViewModel(vm: ChatViewModel): ViewModel

}
