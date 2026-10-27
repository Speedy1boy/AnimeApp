package com.krivykh.animeapp.di

import androidx.room.Room
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.krivykh.animeapp.KitsuApp
import com.krivykh.animeapp.data.local.FilterBadgeCache
import com.krivykh.animeapp.data.local.FilterDataStore
import com.krivykh.animeapp.data.local.ProfileDataStore
import com.krivykh.animeapp.data.local.room.AppDatabase
import com.krivykh.animeapp.data.remote.KitsuApi
import com.krivykh.animeapp.data.repository.AnimeRepositoryImpl
import com.krivykh.animeapp.domain.repository.AnimeRepository
import com.krivykh.animeapp.domain.usecase.GetAnimeDetailsUseCase
import com.krivykh.animeapp.domain.usecase.GetAnimeListUseCase
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AppModule {
    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(ChuckerInterceptor(KitsuApp.appContext))
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://kitsu.app/api/edge/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val kitsuApi: KitsuApi by lazy {
        retrofit.create(KitsuApi::class.java)
    }

    private val database: AppDatabase by lazy {
        Room.databaseBuilder(
            KitsuApp.appContext,
            AppDatabase::class.java,
            "anime_database"
        ).build()
    }

    val filterDataStore by lazy {
        FilterDataStore(KitsuApp.appContext)
    }

    val filterBadgeCache by lazy {
        FilterBadgeCache()
    }

    val repository: AnimeRepository by lazy {
        AnimeRepositoryImpl(kitsuApi, database.animeDao())
    }

    val getAnimeListUseCase by lazy {
        GetAnimeListUseCase(repository)
    }

    val getAnimeDetailsUseCase by lazy {
        GetAnimeDetailsUseCase(repository)
    }

    val profileDataStore by lazy {
        ProfileDataStore(KitsuApp.appContext)
    }
}