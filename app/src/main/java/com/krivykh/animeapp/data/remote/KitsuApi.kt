package com.krivykh.animeapp.data.remote

import com.krivykh.animeapp.data.remote.dto.AnimeListResponseDto
import com.krivykh.animeapp.data.remote.dto.AnimeSingleResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface KitsuApi {
    @GET("anime")
    suspend fun getAnimeList(
        @Query("page[limit]") limit: Int = 20
    ): AnimeListResponseDto

    @GET("anime/{id}")
    suspend fun getAnimeById(
        @Path("id") id: String
    ): AnimeSingleResponseDto
}