package com.helio.chatbot.presentation.elements.dialogs.base

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.helio.chatbot.presentation.R
import com.helio.chatbot.presentation.theme.Colors
import com.helio.chatbot.presentation.theme.Fonts
import rippleClickable


@Preview
@Composable
private fun PreviewBaseDialog() {
    BaseDialog(
        "prev", "prev", {},
    ) {
        addBlackButton(
            text = "prev",
            onClick = {}
        )
        addWhiteButton(
            text = "prev",
            onClick = {}
        )
    }
}


internal class BaseDialogButtonSpec(
    val text: String,
    val textColor: Color,
    val backgroundColor: Color,
    val onClick: ()->Unit,
)

class BaseDialogScope internal constructor() {
    private val _buttons = mutableListOf<BaseDialogButtonSpec>()
    internal val buttons: List<BaseDialogButtonSpec> get() = _buttons

    fun addButton(
        text: String,
        textColor: Color,
        backgroundColor: Color,
        onClick: ()->Unit,
    ) { _buttons.add(BaseDialogButtonSpec(text, textColor, backgroundColor, onClick)) }

}

private val BaseDialogBackgroundShape = RoundedCornerShape(10.dp)
private val BaseDialogModifier = Modifier
    .fillMaxWidth()
    .background(color = Colors.White, shape = BaseDialogBackgroundShape)

private val BaseDialogButtonBackgroundShape = RoundedCornerShape(6.dp)

@Composable
fun BaseDialog(
    headerText: String? = null,
    bodyText: String? = null,
    onDismissRequest: () -> Unit,
    properties: DialogProperties = DialogProperties(),
    buttons: @Composable BaseDialogScope.()->Unit
) = Dialog(
    onDismissRequest = onDismissRequest,
    properties = properties
) {

    val buttonsMaterialized = BaseDialogScope().apply { this.buttons() }

    Column(
        modifier = BaseDialogModifier
    ) {
        headerText?.let {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = headerText,
                    modifier = Modifier.align(Alignment.CenterStart),
                    fontFamily = Fonts.Inter,
                    fontWeight = FontWeight(500),
                    color = Colors.Dark,
                    fontSize = 16.sp
                )
                Icon(
                    modifier = Modifier
                        .rippleClickable { onDismissRequest() }
                        .align(alignment = Alignment.CenterEnd)
                    ,
                    painter = painterResource(id = R.drawable.icon_dialog_cross),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            }
            Divider()
        }
        bodyText?.let {
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            ) {
                Text(
                    text = bodyText,
                    modifier = Modifier.align(Alignment.CenterStart),
                    fontFamily = Fonts.Manrope,
                    fontWeight = FontWeight(400),
                    color = Colors.Dark,
                    fontSize = 16.sp
                )
            }
            Divider()
        }
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            buttonsMaterialized.buttons.forEach { ButtonElement(it) }
        }

    }

}

@Composable
private fun ButtonElement(buttonSpec: BaseDialogButtonSpec) = with(buttonSpec) {
    Text(
        modifier = Modifier
            .clip(BaseDialogButtonBackgroundShape)
            .background(color = backgroundColor, BaseDialogButtonBackgroundShape)
            .rippleClickable { onClick() }
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        text = text,
        color = textColor,
        textAlign = TextAlign.Center,
        fontFamily = Fonts.Manrope,
        fontWeight = FontWeight(600),
        fontSize = 16.sp
    )
}

private val DividerModifier = Modifier
    .fillMaxWidth()
    .height(1.dp)
    .background(color = Color(0xFFCDCED4))

@Composable
private fun Divider() {
    Box(DividerModifier)
}
