package com.krivykh.animeapp.domain.repository

import com.krivykh.animeapp.domain.model.Anime
import kotlinx.coroutines.flow.Flow

interface AnimeRepository {
    fun getAnimeList(): Flow<List<Anime>>
    suspend fun getAnimeById(id: String): Anime
}