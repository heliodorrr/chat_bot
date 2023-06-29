package com.helio.chatbot.presentation.activity

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


open class DiActivity : ComponentActivity() {
    private val vm by viewModels<DiViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return DiViewModel(this@DiActivity) as T
                }
            }
        }
    )
    val presentationComponent get() = vm.chatComponent
}