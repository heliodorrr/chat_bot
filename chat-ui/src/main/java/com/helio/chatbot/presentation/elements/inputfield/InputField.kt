package com.helio.chatbot.presentation.elements.inputfield

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import clippedRippleClickable
import com.helio.chatbot.common.AsyncState
import com.helio.chatbot.domain.model.SpeechRecognitionEvent
import com.helio.chatbot.presentation.Modifiers
import com.helio.chatbot.presentation.R
import com.helio.chatbot.presentation.elements.HSpacer
import com.helio.chatbot.presentation.elements.dialogs.base.BaseDialog
import com.helio.chatbot.presentation.elements.dialogs.base.addBlackButton
import com.helio.chatbot.presentation.elements.dialogs.base.addWhiteButton
import com.helio.chatbot.presentation.elements.inputfield.InputFieldConstants.BgModifier
import com.helio.chatbot.presentation.elements.inputfield.InputFieldConstants.FgModifier
import com.helio.chatbot.presentation.elements.inputfield.InputFieldConstants.PdfMime
import com.helio.chatbot.presentation.text.TextFilters
import com.helio.chatbot.presentation.theme.Colors
import com.helio.chatbot.presentation.theme.TextStyles
import kotlinx.coroutines.flow.Flow
import noIndicationClickable
import rippleClickable


@Preview
@Composable
private fun InputFieldPreview() {
    Box(Modifier.fillMaxSize()) {

    }
}

@Composable
fun InputField(
    modifier: Modifier,
    enabled: Boolean,
    onSend: (String)->Unit,
    onSpeechRecognitionButton: ()->Unit,
    speechRecognitionFlow: Flow<SpeechRecognitionEvent>,
    onExportAsPdf: (Uri)->Unit,
    pdfExportEnabled: Boolean,
    pdfExportState: AsyncState<Unit>?
) = Column(modifier) {

    val speechRecognitionEvent by speechRecognitionFlow
        .collectAsStateWithLifecycle(null)

    if (pdfExportEnabled) {
        PdfExport(onExportAsPdf = onExportAsPdf)
    }

    Row(
        modifier = BgModifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Field(
            modifier = FgModifier
                .weight(1f)
                .fillMaxWidth(),
            enabled = enabled,
            speechRecognitionEvent = speechRecognitionEvent,
            onSend = onSend
        )
        HSpacer(8.dp)
        SpeechRecognizeButton(
            modifier = Modifier,
            onClick = onSpeechRecognitionButton,
            event = speechRecognitionEvent
        )

    }

}

@Composable
private fun Field(
    modifier: Modifier,
    enabled: Boolean,
    speechRecognitionEvent: SpeechRecognitionEvent?,
    onSend: (String)->Unit,
) = ConstraintLayout(modifier) {
    val textRef = createRef()
    val sendRef = createRef()


    var content by remember { mutableStateOf(TextFieldValue("")) }

    LaunchedEffect(speechRecognitionEvent) {
        speechRecognitionEvent?.let {
            if (speechRecognitionEvent is SpeechRecognitionEvent.Text) {
                content = TextFieldValue(
                    text = content.text + speechRecognitionEvent.text,
                    selection = if (content.selection.collapsed) {
                        TextRange(content.selection.start + speechRecognitionEvent.text.length)
                    } else {
                        content.selection
                    }
                )
            }
        }
    }

    TextComponent(
        modifier = Modifier
            .constrainAs(textRef) {
                width = Dimension.fillToConstraints
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(sendRef.start)
            },
        onTextChanged = { content = it },
        text = content
    )

    Icon(
        painter = painterResource(R.drawable.send),
        modifier = Modifier
            .constrainAs(sendRef) {
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }
            .noIndicationClickable(
                enabled = enabled
            ) {
                if (TextFilters.NonEmpty(content.text)) {
                    onSend(content.text)
                    content = TextFieldValue("")
                }
            },
        contentDescription = null,
        tint = Color.Unspecified
    )
}

//TODO: Extract gradient colors
private val SpeechIconAnimationSpec = tween<Color>(durationMillis = 400)
private val SpeechIconAnimationOuterSpec = tween<Color>(durationMillis = 1000)


@Composable
private fun SpeechRecognizeButton(
    modifier: Modifier,
    event: SpeechRecognitionEvent?,
    onClick: ()->Unit
) {

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) { onClick() }
    }

    val center by animateColorAsState(
        targetValue =
        if (event is SpeechRecognitionEvent.Terminal?) { Colors.DarkGray }
        else { Colors.LightGray },
        animationSpec = SpeechIconAnimationSpec,
    )

    val outer by animateColorAsState(
        targetValue =
        if (event is SpeechRecognitionEvent.Terminal?) { Colors.DarkGray }
        else { Colors.MidGray },
        animationSpec = SpeechIconAnimationOuterSpec,
    )


    Icon(
        painter = painterResource(id = R.drawable.icon_mic),
        contentDescription = null,
        modifier = modifier
            .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
            .drawWithCache {
                onDrawWithContent {
                    drawContent()
                    drawRect(
                        brush = Brush.radialGradient(
                            0f to center, 1f to outer
                        ),
                        blendMode = BlendMode.SrcAtop
                    )
                }
            }
            .size(35.dp)
            .clippedRippleClickable {
                launcher.launch(Manifest.permission.RECORD_AUDIO)
            }
            .padding(5.dp)
        ,
    )
}

@Composable
private fun TextComponent(
    modifier: Modifier,
    onTextChanged: (TextFieldValue)->Unit,
    text: TextFieldValue
) {
    BasicTextField(
        value = text,
        onValueChange = onTextChanged,
        modifier = modifier,
        maxLines = 3,
        textStyle = InputFieldConstants.InputFieldTextStyle,
    )
}

@Composable
private fun PdfExport(
    onExportAsPdf: (Uri)->Unit
) {

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument(PdfMime),
    ) {

        it?.let(onExportAsPdf)
    }

    var dialogState by remember { mutableStateOf(false) }

    if (dialogState) {
        PdfExportDialog(
            onDismissRequest = { dialogState = false },
            launch = { launcher.launch("") }
        )
    }

    Row(
        modifier = Modifiers.FillMaxWidth
            .rippleClickable { dialogState = true }
            .background(color = InputFieldConstants.ExportAsColor)
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.export_as),
            style = TextStyles.MessageTextStyle,
            color = Colors.Dark
        )
        HSpacer(width = 12.dp)
        Icon(painter = painterResource(id = R.drawable.pdf_export), contentDescription = "")
    }

}

@Composable
private fun PdfExportDialog(
    onDismissRequest: ()->Unit,
    launch: ()->Unit,
) {

    BaseDialog(
        headerText = stringResource(id = R.string.export_as_pdf_dialog_header),
        bodyText = stringResource(id = R.string.export_as_pdf_dialog_body),
        onDismissRequest = onDismissRequest
    ) {
        addBlackButton(
            text = stringResource(id = R.string.export_as_pdf_dialog_ok),
            onClick = {
                launch()
                onDismissRequest()
            }
        )

        addWhiteButton(
            text = stringResource(id = R.string.export_as_pdf_dialog_cancel),
            onClick = onDismissRequest
        )
    }

}

