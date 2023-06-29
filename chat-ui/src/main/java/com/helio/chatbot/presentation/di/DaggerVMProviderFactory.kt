package com.helio.chatbot.presentation.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.helio.chatbot.presentation.di.component.ChatScope
import javax.inject.Inject
import javax.inject.Provider

@ChatScope
class DaggerVMProviderFactory @Inject constructor(
    private val vmMap: MutableMap<Class<out ViewModel>, Provider<ViewModel>>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val vm = vmMap[modelClass]!!.get()
        return vm as T
    }
}

