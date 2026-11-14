package com.krivykh.animeapp

import android.app.Application
import android.content.Context
import com.krivykh.animeapp.core.CoreProvider

class KitsuApp : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        CoreProvider.init(this)
    }

    companion object {
        lateinit var appContext: Context
            private set
    }
}