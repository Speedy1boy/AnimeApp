package com.krivykh.animeapp.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String) {
    object AnimeList : Screen("anime_list")
    object Details : Screen("anime_details/{animeId}") {
        fun createRoute(animeId: String) = "anime_details/$animeId"
    }
    object Settings : Screen("settings")
}

sealed class BottomNavItem(
    val screen: Screen,
    val title: String,
    val icon: ImageVector
) {
    object AnimeList : BottomNavItem(Screen.AnimeList, "Аниме", Icons.AutoMirrored.Filled.List)
    object Settings : BottomNavItem(Screen.Settings, "Настройки", Icons.Default.Settings)
}