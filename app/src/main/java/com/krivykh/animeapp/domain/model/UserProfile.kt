package com.krivykh.animeapp.domain.model

data class UserProfile(
    val fullName: String = "",
    val jobTitle: String = "",
    val avatarUri: String = "",
    val resumeUrl: String = ""
)