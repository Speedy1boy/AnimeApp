package com.krivykh.animeapp.data.repository

import com.krivykh.animeapp.domain.model.Anime
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

class MockAnimeRepository {
    private val animeList = buildList {
        addAll(
            (1..10).map { index ->
                Anime(
                    id = index.toString(),
                    title = "Samurai Champloo (Mock $index)",
                    synopsis = "Девушка по имени Фуу спасает двух совершенно разных мечников - бродягу Мугена и ронина Дзина от казни. " +
                            "Взамен она просит их помочь ей найти \"самурая, пахнущего подсолнухами\". " +
                            "Это длинный текст-заглушка для проверки работы ConstraintLayout и скролла на экране деталей под номером $index.",
                    posterImageUrl = "https://placehold.co/400x600/292929/E2E2E2.png?text=Champloo+\\n$index",
                    averageRating = "8.${index % 9} / 10",
                    episodeCount = 26,
                    status = if (index % 2 == 0) "Finished" else "Currently Airing"
                )
            }
        )
    }

    suspend fun getAnimeList(): List<Anime> {
        delay(1000.milliseconds)
        return animeList
    }

    suspend fun getAnimeById(id: String): Anime {
        delay(500.milliseconds)
        return animeList.find { it.id == id }
            ?: throw IllegalArgumentException("Anime with id $id not found")
    }
}