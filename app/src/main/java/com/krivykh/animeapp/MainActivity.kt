package com.krivykh.animeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.krivykh.animeapp.presentation.navigation.MainScreen
import com.krivykh.animeapp.presentation.theme.AnimeAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnimeAppTheme {
                MainScreen()
            }
        }
    }
}