package com.krivykh.animeapp.di

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.krivykh.animeapp.KitsuApp
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

    val repository: AnimeRepository by lazy {
        AnimeRepositoryImpl(kitsuApi)
    }

    val getAnimeListUseCase by lazy {
        GetAnimeListUseCase(repository)
    }

    val getAnimeDetailsUseCase by lazy {
        GetAnimeDetailsUseCase(repository)
    }
}