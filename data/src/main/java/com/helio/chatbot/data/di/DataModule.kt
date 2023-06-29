package com.helio.chatbot.data.di

import android.content.Context
import androidx.room.Room
import com.aallam.openai.client.OpenAI
import com.helio.chatbot.common.di.AppContext
import com.helio.chatbot.common.di.MainComponentScope
import com.helio.chatbot.data.database.AppDatabase
import com.helio.chatbot.data.repository.ChatLocalRepositoryImpl
import com.helio.chatbot.data.repository.ChatRemoteRepositoryImpl
import com.helio.chatbot.data.repository.ExportPdfRepositoryImpl
import com.helio.chatbot.domain.repository.ChatLocalRepository
import com.helio.chatbot.domain.repository.ChatRemoteRepository
import com.helio.chatbot.domain.repository.ExportPdfRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @Binds
    fun bindChatLocalRepository(bind: ChatLocalRepositoryImpl): ChatLocalRepository

    @Binds
    fun bindChatRemoteRepository(bind: ChatRemoteRepositoryImpl): ChatRemoteRepository

    @Binds
    fun bindExportPdfRepository(bind: ExportPdfRepositoryImpl): ExportPdfRepository

    companion object {
        @Provides
        @MainComponentScope
        fun openAI(): OpenAI {
            return OpenAI("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")
        }

        @Provides
        @MainComponentScope
        fun database(@AppContext context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "app_db")
                .build()
        }

        @MainComponentScope
        @Provides
        fun messageDao(db: AppDatabase) = db.messageDao()
    }


}