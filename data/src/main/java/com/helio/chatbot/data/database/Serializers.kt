package com.helio.chatbot.data.database

import androidx.room.TypeConverter
import java.time.OffsetDateTime


object Serializers {

    @TypeConverter
    fun offsetDateTimeToString(value: OffsetDateTime): String {
        return value.toString()
    }

    @TypeConverter
    fun offsetDateTimeFromString(value: String): OffsetDateTime {
        return OffsetDateTime.parse(value)
    }

}