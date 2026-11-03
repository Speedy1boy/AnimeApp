package com.krivykh.animeapp.domain.repository

import com.krivykh.animeapp.domain.model.Anime
import kotlinx.coroutines.flow.Flow

interface AnimeRepository {
    fun getAnimeList(searchQuery: String, status: String): Flow<List<Anime>>
    suspend fun getAnimeById(id: String): Anime
    fun getFavorites(): Flow<List<Anime>>
    fun isFavorite(id: String): Flow<Boolean>
    suspend fun addFavorite(anime: Anime)
    suspend fun removeFavorite(anime: Anime)
}