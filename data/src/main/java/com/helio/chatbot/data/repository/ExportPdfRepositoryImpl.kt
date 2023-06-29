package com.helio.chatbot.data.repository

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import androidx.core.graphics.withTranslation

import com.helio.chatbot.common.di.AppContext
import com.helio.chatbot.data.R
import com.helio.chatbot.domain.model.MessageAuthor
import com.helio.chatbot.domain.model.MessageModel
import com.helio.chatbot.domain.repository.ExportPdfRepository
import javax.inject.Inject

class ExportPdfRepositoryImpl @Inject constructor(
    @AppContext private val context: Context,
): ExportPdfRepository {

    /**
     * Responsible for producing pdf document from messages history.
     */
    override suspend fun produceAndExportPdf(uri: Uri, messages: List<MessageModel>) {
        context.contentResolver.openOutputStream(uri)?.use { os->
            PdfProducer(context.resources, messages)
                .produceDocument { pdfDoc-> pdfDoc.writeTo(os) }
        }

    }
}


internal class PdfProducer(
    private val resources: Resources,
    messages: List<MessageModel>
) {

    companion object {
        private val FONT_RES = R.font.inter_400
        private const val FONT_SIZE = 24f
        private const val MESSAGE_TEXT_PADDING = 25f
        private const val INTER_MESSAGE_MARGIN = 25f
        private const val SIDE_MARGIN = 40f
        private const val ROUNDING = 10f

        private const val PAGE_WIDTH = 595
        private const val PAGE_HEIGHT = 842

        private val BG_COLOR = Color.argb(255, 14, 14, 27)
        private val SYSTEM_MESSAGE_COLOR = Color.argb(255, 27, 27, 35)
        private val MY_MESSAGE_COLOR = Color.argb(255, 103, 104, 116)
        private val MESSAGE_TEXT_COLOR = Color.argb(255, 233, 233, 233)


        const val SPACE_CHAR = ' '
        const val LINE_BREAK_CHAR = '\n'
    }

    private val _meRadii = floatArrayOf(ROUNDING, ROUNDING, 0f, 0f,  0f,  0f, ROUNDING, ROUNDING)
    private val _othersRadii = floatArrayOf(0f, 0f, ROUNDING, ROUNDING, ROUNDING ,ROUNDING ,0f, 0f)

    private val _flatTopMeRadii = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, ROUNDING, ROUNDING)
    private val _flatBottomMeRadii =   floatArrayOf(ROUNDING, ROUNDING, 0f, 0f, 0f, 0f, 0f, 0f)

    private val _flatTopOtherRadii =   floatArrayOf(0f, 0f, 0f, 0f, ROUNDING, ROUNDING, 0f, 0f)
    private val _flatBottomOtherRadii = floatArrayOf(0f, 0f, ROUNDING, ROUNDING, 0f, 0f, 0f, 0f)

    private val _flatRadii = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)

    private val reusableRectF = RectF()

    private val _myMessagePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        .apply { color = MY_MESSAGE_COLOR }

    private val _systemMessagePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        .apply { color = SYSTEM_MESSAGE_COLOR }

    private val _textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        .apply {
            color = MESSAGE_TEXT_COLOR
            textSize = FONT_SIZE
            typeface = resources.getFont(FONT_RES)
        }

    private val _bgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        .apply { color = BG_COLOR }

    private val messagesDeq = messages.map { messageModel -> SplitMessage(messageModel) }


    suspend fun produceDocument(
        doWithDocument: (PdfDocument)->Unit
    ) {
        val document = PdfDocument()
        try {

            producePages(document, 1, 0, false)

            doWithDocument(document)

        } finally {
            document.close()
        }
    }

    private fun calculateMaxLayout(
        messageParts: ArrayDeque<String>, heightBound: Int, width: Int
    ): StaticLayout? {
        val sb = StringBuilder()
        var lastLayout: StaticLayout? = null
        var wordsCount = 0
        while (true) {
            if (messageParts.isEmpty()) {
                break
            }
            if (sb.isNotEmpty()) { sb.append(SPACE_CHAR) }
            sb.append(messageParts.first())
            val sl = StaticLayout.Builder
                .obtain(sb, 0, sb.length, _textPaint, width)
                .build()
            if (sl.height <= heightBound) {
                messageParts.removeFirst()
                lastLayout = sl
                wordsCount++
            } else {
                break;
            }
        }
        return lastLayout
    }

    private fun producePages(
        document: PdfDocument,
        pageNumber: Int,
        startMessageIndex: Int,
        isContinue: Boolean
    ) {

        if (pageNumber > 100) { error("Document maximum size is 100 pages, sorry :(") }
        if (startMessageIndex >= messagesDeq.size) { return }

        val pageInfo = PdfDocument.PageInfo.Builder(
            PAGE_WIDTH, PAGE_HEIGHT, pageNumber
        ).create()

        val page = document.startPage(pageInfo)
        var cursor = 0f
        var messageIndex = startMessageIndex

        page.canvas.run {

            drawRect(0f, 0f, width.toFloat(), height.toFloat(), _bgPaint)
            val widthBound = (width - SIDE_MARGIN - 2 * MESSAGE_TEXT_PADDING).toInt()

            while (true) {
                if (messageIndex == messagesDeq.size) { document.finishPage(page); return }
                val message = messagesDeq[messageIndex]

                val heightBound
                = (height - cursor - 2 * (INTER_MESSAGE_MARGIN + MESSAGE_TEXT_PADDING)).toInt()

                val maxLayout = calculateMaxLayout(message.words, heightBound, widthBound)

                val messageIsConsumed = message.words.isEmpty()

                if (maxLayout != null) {
                    drawLogic(
                        canvas = this,
                        text = maxLayout,
                        author = message.author,
                        fillTop = isContinue && messageIndex == startMessageIndex,
                        fillBottom = !messageIsConsumed,
                        cursor = cursor
                    )

                    if (!messageIsConsumed) {
                        document.finishPage(page)
                        producePages(document, pageNumber + 1, messageIndex, true)
                        return
                    }

                    cursor += (maxLayout.height + 2 * (MESSAGE_TEXT_PADDING) + INTER_MESSAGE_MARGIN)
                    messageIndex++

                } else {
                    document.finishPage(page)
                    producePages(document, pageNumber + 1, messageIndex, false)
                    return
                }

            }

        }

    }

    private fun Layout.calculateActualWidth(): Float {
        return (0 until lineCount).maxOf { getLineWidth(it) }
    }

    private fun drawLogic(
        canvas: Canvas,
        text: Layout,
        author: MessageAuthor,
        fillTop: Boolean,
        fillBottom: Boolean,
        cursor: Float
    ) = canvas.run {

        val textActualWidth = text.calculateActualWidth()
        val textActualHeight = text.height

        val bgX = when(author) {
            MessageAuthor.SYSTEM -> 0f
            MessageAuthor.ME -> width - 2 * MESSAGE_TEXT_PADDING - textActualWidth
        }

        reusableRectF.apply {

            right = (textActualWidth + (2 * MESSAGE_TEXT_PADDING))
    
            var expectedHeight = (textActualHeight + (2 * MESSAGE_TEXT_PADDING))
            if (fillTop) { expectedHeight += INTER_MESSAGE_MARGIN }
            if (fillBottom) { expectedHeight += INTER_MESSAGE_MARGIN }
            bottom = expectedHeight
        }

        val path = Path().apply {
            addRoundRect(
                reusableRectF,
                when(author) {
                    MessageAuthor.ME -> if (fillTop && fillBottom) {
                        _flatRadii
                    } else if (fillTop) {
                        _flatTopMeRadii
                    } else if (fillBottom) {
                        _flatBottomMeRadii
                    } else {
                        _meRadii
                    }
                    MessageAuthor.SYSTEM -> if (fillTop && fillBottom) {
                        _flatRadii
                    } else if (fillTop) {
                        _flatTopOtherRadii
                    } else if (fillBottom) {
                        _flatBottomOtherRadii
                    } else {
                        _othersRadii
                    }
                },
                Path.Direction.CW
            )
        }
        canvas.withTranslation(bgX, if (fillTop) cursor else cursor + INTER_MESSAGE_MARGIN) {
            drawPath(path, when(author) {
                MessageAuthor.ME -> _myMessagePaint
                MessageAuthor.SYSTEM -> _systemMessagePaint
            })
        }

        val textX = bgX + MESSAGE_TEXT_PADDING
        canvas.withTranslation(textX, cursor + MESSAGE_TEXT_PADDING + INTER_MESSAGE_MARGIN) {
            text.draw(this)
        }

    }

}

private class SplitMessage(
    message: MessageModel
) {

    val words: ArrayDeque<String> = splitWords(message.content)
    val author = message.author

    private fun splitWords(message: String): ArrayDeque<String> {
        val result = ArrayDeque<String>()

        fun Int.isSpace(): Boolean { return this == PdfProducer.SPACE_CHAR.code }
        fun Int.isBreak(): Boolean { return this == PdfProducer.LINE_BREAK_CHAR.code }
        fun Int.isSymbol(): Boolean { return !isSpace() && !isBreak() }
        fun Int.isNothing(): Boolean { return this == -1}

        val sb = StringBuilder()

        fun flushWord() {
            result.add(sb.toString())
            sb.clear()
        }

        var i = 0
        while (i < message.length) {
            val currentChar = message[i].code
            val nextChar = if (i + 1 < message.length) { message[i + 1].code } else { -1 }

            sb.append(currentChar.toChar())

            if (nextChar.isNothing()) {
                flushWord()
            } else if (currentChar.isSpace()) {
                if (nextChar.isBreak()) {
                    sb.append(nextChar)
                    flushWord()
                    i++
                } else if(nextChar.isSymbol()) {
                    flushWord()
                }
            } else if(currentChar.isBreak()) {
                flushWord()
            }

            i++
        }

        return result
    }

}