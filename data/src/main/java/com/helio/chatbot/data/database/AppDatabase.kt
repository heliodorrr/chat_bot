package com.helio.chatbot.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.helio.chatbot.data.model.MessageEntity

@Database(
    entities = [MessageEntity::class],
    version = 1
)
@TypeConverters(Serializers::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun messageDao(): MessageDao
}