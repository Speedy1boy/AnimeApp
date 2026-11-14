package com.krivykh.animeapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AnimeListResponseDto(
    @SerializedName("data") val data: List<AnimeDataDto>
)

data class AnimeSingleResponseDto(
    @SerializedName("data") val data: AnimeDataDto
)

data class AnimeDataDto(
    @SerializedName("id") val id: String,
    @SerializedName("attributes") val attributes: AnimeAttributesDto
)

data class AnimeAttributesDto(
    @SerializedName("canonicalTitle") val title: String?,
    @SerializedName("synopsis") val synopsis: String?,
    @SerializedName("averageRating") val averageRating: String?,
    @SerializedName("episodeCount") val episodeCount: Int?,
    @SerializedName("status") val status: String?,
    @SerializedName("posterImage") val posterImage: PosterImageDto?
)

data class PosterImageDto(
    @SerializedName("original") val original: String?,
    @SerializedName("small") val small: String?
)