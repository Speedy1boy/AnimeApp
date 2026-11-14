package com.krivykh.animeapp.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krivykh.animeapp.data.local.FilterSettings
import com.krivykh.animeapp.di.AppModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {
    private val dataStore = AppModule.filterDataStore
    private val badgeCache = AppModule.filterBadgeCache

    private val _uiState = MutableStateFlow(FilterSettings())
    val uiState: StateFlow<FilterSettings> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            dataStore.filterSettingsFlow.collect { settings ->
                _uiState.value = settings
                updateBadge(settings)
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun onStatusChanged(status: String) {
        _uiState.value = _uiState.value.copy(status = status)
    }

    fun saveSettings() {
        viewModelScope.launch {
            dataStore.saveFilters(
                searchQuery = _uiState.value.searchQuery,
                status = _uiState.value.status
            )
            updateBadge(_uiState.value)
        }
    }

    private fun updateBadge(settings: FilterSettings) {
        val hasFilters = settings.searchQuery.isNotBlank() || settings.status != "All"
        badgeCache.updateBadgeVisibility(hasFilters)
    }
}