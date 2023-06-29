package com.helio.chatbot.presentation.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.helio.chatbot.presentation.navigation.Graph
import com.helio.chatbot.presentation.theme.ChatBotTheme

class MainActivity : DiActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ChatBotTheme {
                Graph(
                    diFactory = presentationComponent.daggerViewModelFactory(),
                    navController = rememberNavController()
                )
            }
        }
    }
}



 