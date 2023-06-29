package com.helio.chatbot.presentation.text

typealias TextPredicate = (String)->Boolean

object TextFilters {
    val NonEmpty: TextPredicate = String::isNotEmpty
}