package com.krivykh.animeapp.domain.usecase

import com.krivykh.animeapp.domain.model.Anime
import com.krivykh.animeapp.domain.repository.AnimeRepository
import kotlinx.coroutines.flow.Flow

class GetAnimeListUseCase(
    private val repository: AnimeRepository
) {
    operator fun invoke(): Flow<List<Anime>> {
        return repository.getAnimeList()
    }
}

class GetAnimeDetailsUseCase(
    private val repository: AnimeRepository
) {
    suspend operator fun invoke(id: String): Anime {
        return repository.getAnimeById(id)
    }
}