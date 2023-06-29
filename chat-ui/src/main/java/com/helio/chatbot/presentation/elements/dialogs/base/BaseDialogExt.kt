package com.helio.chatbot.presentation.elements.dialogs.base

import com.helio.chatbot.presentation.theme.Colors

fun BaseDialogScope.addBlackButton(text: String, onClick: ()->Unit) {
    addButton(
        text = text,
        onClick = onClick,
        backgroundColor = Colors.Dark,
        textColor = Colors.White
    )
}

fun BaseDialogScope.addWhiteButton(text: String, onClick: ()->Unit) {
    addButton(
        text = text,
        onClick = onClick,
        backgroundColor = Colors.LightGray,
        textColor = Colors.Dark
    )
}