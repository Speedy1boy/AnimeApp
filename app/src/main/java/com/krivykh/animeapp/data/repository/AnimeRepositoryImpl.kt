package com.krivykh.animeapp.data.repository

import com.krivykh.animeapp.data.remote.KitsuApi
import com.krivykh.animeapp.data.remote.dto.toDomain
import com.krivykh.animeapp.domain.model.Anime
import com.krivykh.animeapp.domain.repository.AnimeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class AnimeRepositoryImpl(
    private val api: KitsuApi
) : AnimeRepository {
    private var cachedAnimeList: List<Anime>? = null

    override fun getAnimeList(): Flow<List<Anime>> = flow {
        cachedAnimeList?.let { emit(it) }

        try {
            val response = api.getAnimeList(limit = 20)
            val domainList = response.data.map { it.toDomain() }

            cachedAnimeList = domainList

            emit(domainList)
        } catch (e: Exception) {
            if (cachedAnimeList.isNullOrEmpty()) {
                throw e
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getAnimeById(id: String): Anime {
        return withContext(Dispatchers.IO) {
            val response = api.getAnimeById(id)
            response.data.toDomain()
        }
    }
}