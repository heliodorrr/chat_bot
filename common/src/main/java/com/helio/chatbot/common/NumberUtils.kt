package com.helio.chatbot.common


inline val Float.coerceAsFraction get() = coerceIn(0f, 1f)