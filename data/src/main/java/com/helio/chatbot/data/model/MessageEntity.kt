package com.helio.chatbot.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.helio.chatbot.domain.model.MessageModel
import com.helio.chatbot.domain.model.MessageAuthor
import java.time.OffsetDateTime

@Entity(
    tableName = "message"
)
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val id: Long = 0,
    @ColumnInfo("content")
    val content: String,
    @ColumnInfo("date_time")
    val dateTime: OffsetDateTime,
    @ColumnInfo("type")
    val type: MessageAuthor
) {


    fun toMessageModel() = MessageModel(
        content = content,
        dateTime = dateTime,
        author = type
    )

    companion object {

        fun fromMessageModel(messageModel: MessageModel) = with(messageModel) {
            MessageEntity(
                content = content,
                dateTime = dateTime,
                type = author
            )
        }




    }





}
