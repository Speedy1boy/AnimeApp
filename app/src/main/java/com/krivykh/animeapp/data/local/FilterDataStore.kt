package com.krivykh.animeapp.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

data class FilterSettings(
    val searchQuery: String = "",
    val status: String = "All"
)

class FilterDataStore(private val context: Context) {
    private val SEARCH_QUERY_KEY = stringPreferencesKey("search_query")
    private val STATUS_KEY = stringPreferencesKey("status")

    val filterSettingsFlow: Flow<FilterSettings> = context.dataStore.data.map { preferences ->
        FilterSettings(
            searchQuery = preferences[SEARCH_QUERY_KEY] ?: "",
            status = preferences[STATUS_KEY] ?: "All"
        )
    }

    suspend fun saveFilters(searchQuery: String, status: String) {
        context.dataStore.edit { preferences ->
            preferences[SEARCH_QUERY_KEY] = searchQuery
            preferences[STATUS_KEY] = status
        }
    }
}