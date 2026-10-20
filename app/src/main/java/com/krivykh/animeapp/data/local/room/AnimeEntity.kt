package com.krivykh.animeapp.data.local.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.krivykh.animeapp.domain.model.Anime

@Entity(tableName = "favorites")
data class AnimeEntity(
    @PrimaryKey val id: String,
    val title: String,
    val synopsis: String,
    val posterImageUrl: String,
    val averageRating: String,
    val episodeCount: Int,
    val status: String
)

fun AnimeEntity.toDomain(): Anime {
    return Anime(id, title, synopsis, posterImageUrl, averageRating, episodeCount, status)
}

fun Anime.toEntity(): AnimeEntity {
    return AnimeEntity(id, title, synopsis, posterImageUrl, averageRating, episodeCount, status)
}