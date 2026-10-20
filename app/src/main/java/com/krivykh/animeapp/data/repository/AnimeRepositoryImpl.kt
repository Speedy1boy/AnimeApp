package com.krivykh.animeapp.data.repository

import com.krivykh.animeapp.data.local.room.AnimeDao
import com.krivykh.animeapp.data.local.room.toDomain
import com.krivykh.animeapp.data.local.room.toEntity
import com.krivykh.animeapp.data.remote.KitsuApi
import com.krivykh.animeapp.data.remote.dto.toDomain
import com.krivykh.animeapp.domain.model.Anime
import com.krivykh.animeapp.domain.repository.AnimeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class AnimeRepositoryImpl(
    private val api: KitsuApi,
    private val dao: AnimeDao
) : AnimeRepository {

    override fun getAnimeList(searchQuery: String, status: String): Flow<List<Anime>> = flow {
        val query = searchQuery.ifBlank { null }
        val response = api.getAnimeList(limit = 20, searchQuery = query)
        var domainList = response.data.map { it.toDomain() }

        if (status != "All") {
            domainList = domainList.filter { it.status.equals(status, ignoreCase = true) }
        }

        emit(domainList)
    }.flowOn(Dispatchers.IO)

    override suspend fun getAnimeById(id: String): Anime {
        return withContext(Dispatchers.IO) {
            val response = api.getAnimeById(id)
            response.data.toDomain()
        }
    }

    override fun getFavorites(): Flow<List<Anime>> {
        return dao.getFavorites().map { list -> list.map { it.toDomain() } }
    }

    override fun isFavorite(id: String): Flow<Boolean> {
        return dao.isFavorite(id)
    }

    override suspend fun addFavorite(anime: Anime) {
        withContext(Dispatchers.IO) {
            dao.addFavorite(anime.toEntity())
        }
    }

    override suspend fun removeFavorite(anime: Anime) {
        withContext(Dispatchers.IO) {
            dao.removeFavorite(anime.toEntity())
        }
    }
}