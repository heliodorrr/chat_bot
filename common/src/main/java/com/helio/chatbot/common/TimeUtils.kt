package com.helio.chatbot.common

import java.time.OffsetDateTime
import java.time.temporal.ChronoField
import java.time.temporal.Temporal

fun OffsetDateTime.toEpochMilli(): Long {
    return toEpochSecond() * 1000  + nano / 100_000
}

val Temporal.epochDay: Long get() {
    return getLong(ChronoField.EPOCH_DAY)
}