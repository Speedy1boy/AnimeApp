package com.krivykh.animeapp.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.krivykh.animeapp.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.profileDataStore: DataStore<Preferences> by preferencesDataStore(name = "user_profile")

class ProfileDataStore(private val context: Context) {
    private val FULL_NAME_KEY = stringPreferencesKey("full_name")
    private val JOB_TITLE_KEY = stringPreferencesKey("job_title")
    private val AVATAR_URI_KEY = stringPreferencesKey("avatar_uri")
    private val RESUME_URL_KEY = stringPreferencesKey("resume_url")

    val userProfileFlow: Flow<UserProfile> = context.profileDataStore.data.map { preferences ->
        UserProfile(
            fullName = preferences[FULL_NAME_KEY] ?: "",
            jobTitle = preferences[JOB_TITLE_KEY] ?: "",
            avatarUri = preferences[AVATAR_URI_KEY] ?: "",
            resumeUrl = preferences[RESUME_URL_KEY] ?: ""
        )
    }

    suspend fun saveProfile(profile: UserProfile) {
        context.profileDataStore.edit { preferences ->
            preferences[FULL_NAME_KEY] = profile.fullName
            preferences[JOB_TITLE_KEY] = profile.jobTitle
            preferences[AVATAR_URI_KEY] = profile.avatarUri
            preferences[RESUME_URL_KEY] = profile.resumeUrl
        }
    }
}