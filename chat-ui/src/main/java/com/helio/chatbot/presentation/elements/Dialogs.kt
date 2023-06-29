package com.helio.chatbot.presentation.elements

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.helio.chatbot.presentation.R
import com.helio.chatbot.presentation.elements.dialogs.base.BaseDialog
import com.helio.chatbot.presentation.elements.dialogs.base.addWhiteButton
import com.helio.chatbot.presentation.theme.Colors

@Preview
@Composable
private fun PreviewSimpleDialog() = MaterialTheme(
    colorScheme = MaterialTheme.colorScheme.copy(primary = Colors.MidGray)
) {
    var popupState by remember { mutableStateOf(false) }

    Button(onClick = { popupState = true }) {
        Text(text = "Click Me")
    }

    if (popupState) {

    }


}

@Composable
fun ClearHistoryDialog(
    onDismiss: ()->Unit,
    onConfirm: ()->Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            Text(
                text = stringResource(id = R.string.dialog_history_clean_body),
                textAlign = TextAlign.Center
            )
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirm()
                onDismiss()
            }) {

                Text(
                    color = MaterialTheme.colorScheme.error,
                    text = stringResource(id = R.string.dialog_yes)
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(

                    text = stringResource(id = R.string.dialog_no)
                )
            }
        },

    )

}

@Composable
fun SomeErrorDialog(
    onDismissRequest: ()->Unit,
) {

    BaseDialog(
        headerText = stringResource(id = R.string.dialog_error_body),
        onDismissRequest = onDismissRequest
    ) {
        addWhiteButton(
            text = stringResource(id = R.string.dialog_ok),
            onClick = onDismissRequest
        )
    }

}



