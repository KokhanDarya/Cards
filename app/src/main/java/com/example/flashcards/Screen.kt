package com.example.flashcards

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val icon: ImageVector) {
    object MenuScreen : Screen("Home", Icons.Default.Face)
    object CardScreen : Screen("card_screen", Icons.Default.List)
    object AddScreen : Screen("Add", Icons.Default.Add)
    object TestScreen : Screen("Test", Icons.Default.Check)
}
