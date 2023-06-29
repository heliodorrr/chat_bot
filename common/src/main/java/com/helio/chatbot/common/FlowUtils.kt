package com.helio.chatbot.common

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

sealed class AsyncState<out T> {
    object Await: AsyncState<Nothing>()
    class Success<T>(val value: T): AsyncState<T>()
    class Error(val throwable: Throwable): AsyncState<Nothing>()
}

fun <T> flowFromCoroutine(block: suspend ()->T) = flow {
    emit(AsyncState.Await)
    try {
        emit(AsyncState.Success(block()))
    } catch (t: Throwable) {
        Log.d("AsyncError","error is ${t.message}\n${t.stackTraceToString()}")
        emit(AsyncState.Error(t))
    }
}

fun <T> flowFromResultCoroutine(block: suspend ()->Result<T>) = flow {
    emit(AsyncState.Await)
    block()
        .onSuccess { emit(AsyncState.Success(it)) }
        .onFailure { emit(AsyncState.Error(it)) }
}