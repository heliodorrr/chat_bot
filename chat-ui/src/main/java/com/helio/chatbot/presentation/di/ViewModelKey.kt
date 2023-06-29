package com.helio.chatbot.presentation.di

import androidx.lifecycle.ViewModel
import dagger.MapKey
import kotlin.reflect.KClass

@MapKey
annotation class ViewModelKey(val vmClass: KClass<out ViewModel>)
