package com.helio.chatbot.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.helio.chatbot.presentation.Modifiers
import com.helio.chatbot.presentation.screens.SplashScreen
import com.helio.chatbot.presentation.screens.chat.ui.ChatScreen
import com.helio.chatbot.presentation.screens.chat.model.ChatViewModel
import com.helio.chatbot.presentation.theme.Colors


private val GraphModifier = Modifiers.FillMaxSize
    .background(Colors.Dark)
    .statusBarsPadding()
    .imePadding()

@Composable
fun Graph(
    diFactory: ViewModelProvider.Factory,
    navController: NavHostController,
) = NavHost(
    navController = navController,
    startDestination = Destinations.Splash,
    modifier = GraphModifier
) {

    composable(route = Destinations.Splash) {
        SplashScreen {
            navController.navigate(
                route = Destinations.Chat,
                navOptions = navOptions {
                    popUpTo(Destinations.Splash) {
                        inclusive = true
                    }
                }
            )
        }
    }

    composable(route = Destinations.Chat) {
        val chatViewModel = viewModel<ChatViewModel>(factory = diFactory)
        ChatScreen(viewModel = chatViewModel)
    }

}