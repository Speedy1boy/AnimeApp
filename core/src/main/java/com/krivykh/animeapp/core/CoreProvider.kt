package com.krivykh.animeapp.core

import android.content.Context
import com.krivykh.animeapp.data.local.ProfileDataStore

object CoreProvider {
    lateinit var appContext: Context
        private set

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    val profileDataStore: ProfileDataStore by lazy {
        ProfileDataStore(appContext)
    }
}