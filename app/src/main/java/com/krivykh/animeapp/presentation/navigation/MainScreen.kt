package com.krivykh.animeapp.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.krivykh.animeapp.di.AppModule
import com.krivykh.animeapp.presentation.anime_details.AnimeDetailsScreen
import com.krivykh.animeapp.presentation.anime_list.AnimeListScreen
import com.krivykh.animeapp.presentation.favorites.FavoritesScreen
import com.krivykh.animeapp.presentation.settings.SettingsScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val isBadgeVisible by AppModule.filterBadgeCache.isBadgeVisible.collectAsState()

    val bottomNavItems = listOf(
        BottomNavItem(Screen.AnimeList, "Аниме", Icons.Default.List),
        BottomNavItem(Screen.Favorites, "Избранное", Icons.Default.Favorite),
        BottomNavItem(Screen.Settings, "Настройки", Icons.Default.Settings)
    )

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            val showBottomBar = bottomNavItems.any { it.screen.route == currentDestination?.route }

            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            icon = {
                                if (item.screen == Screen.Settings && isBadgeVisible) {
                                    BadgedBox(badge = { Badge() }) {
                                        Icon(item.icon, contentDescription = item.title)
                                    }
                                } else {
                                    Icon(item.icon, contentDescription = item.title)
                                }
                            },
                            label = { Text(item.title) },
                            selected = currentDestination?.hierarchy?.any { it.route == item.screen.route } == true,
                            onClick = {
                                navController.navigate(item.screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.AnimeList.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.AnimeList.route) {
                AnimeListScreen(onAnimeClick = { navController.navigate(Screen.Details.createRoute(it)) })
            }
            composable(Screen.Favorites.route) {
                FavoritesScreen(onAnimeClick = { navController.navigate(Screen.Details.createRoute(it)) })
            }
            composable(Screen.Settings.route) {
                SettingsScreen()
            }
            composable(Screen.Details.route) {
                AnimeDetailsScreen(onBackClick = { navController.popBackStack() })
            }
        }
    }
}