package com.krivykh.animeapp.data.remote.dto

import com.krivykh.animeapp.domain.model.Anime

fun AnimeDataDto.toDomain(): Anime {
    return Anime(
        id = this.id,
        title = this.attributes.title ?: "Unknown Title",
        synopsis = this.attributes.synopsis ?: "No synopsis available.",
        posterImageUrl = this.attributes.posterImage?.original ?: this.attributes.posterImage?.small ?: "",
        averageRating = this.attributes.averageRating?.let { "$it / 100" } ?: "N/A",
        episodeCount = this.attributes.episodeCount ?: 0,
        status = this.attributes.status ?: "Unknown"
    )
}