package com.example.flashcards

import android.annotation.SuppressLint
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.flashcards.ui.screens.AddScreen
import com.example.flashcards.ui.screens.CardScreen
import com.example.flashcards.ui.screens.MenuScreen
import com.example.flashcards.ui.screens.TestScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Navigation() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.MenuScreen.route
        ) {
            composable(route = Screen.MenuScreen.route) {
                MenuScreen(
                    onNavigate = { route ->
                        navController.navigate(route)
                    }
                )
            }

            composable(
                route = Screen.CardScreen.route + "/{id}",
                arguments = listOf(
                    navArgument("id") {
                        type = NavType.IntType
                        defaultValue = 0
                        nullable = false
                    }
                )
            ) {
                CardScreen(topicId = it.arguments?.getInt("id") ?: 0)
            }

            composable(route = Screen.AddScreen.route) {
                AddScreen(
                    onNavigate = {
                        navController.popBackStack()
                    }
                )
            }

            composable(route = Screen.TestScreen.route) {
                TestScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        Screen.MenuScreen,
        Screen.AddScreen,
        Screen.TestScreen
    )
    NavigationBar {
        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.route) },
                label = { Text(screen.route) },
                selected = false,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
