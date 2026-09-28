package com.krivykh.animeapp.domain.model

data class Anime(
    val id: String,
    val title: String,
    val synopsis: String,
    val posterImageUrl: String,
    val averageRating: String,
    val episodeCount: Int,
    val status: String
)