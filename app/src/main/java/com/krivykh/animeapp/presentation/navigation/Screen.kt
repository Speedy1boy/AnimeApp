package com.krivykh.animeapp.presentation.navigation

import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String) {
    object AnimeList : Screen("anime_list")
    object Favorites : Screen("favorites")
    object Profile : Screen("profile")
    object EditProfile : Screen("edit_profile")
    object Details : Screen("anime_details/{animeId}") {
        fun createRoute(animeId: String) = "anime_details/$animeId"
    }
    object Settings : Screen("settings")
}

data class BottomNavItem(
    val screen: Screen,
    val title: String,
    val icon: ImageVector
)