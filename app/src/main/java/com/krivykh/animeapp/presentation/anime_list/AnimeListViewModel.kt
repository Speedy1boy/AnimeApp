package com.krivykh.animeapp.presentation.anime_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krivykh.animeapp.di.AppModule
import com.krivykh.animeapp.domain.model.Anime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.catch

data class AnimeListUiState(
    val isLoading: Boolean = true,
    val animeList: List<Anime> = emptyList(),
    val errorMessage: String? = null
)

class AnimeListViewModel : ViewModel() {
    private val getAnimeListUseCase = AppModule.getAnimeListUseCase

    private val _uiState = MutableStateFlow(AnimeListUiState())
    val uiState: StateFlow<AnimeListUiState> = _uiState.asStateFlow()

    private val dataStore = AppModule.filterDataStore

    init {
        viewModelScope.launch {
            dataStore.filterSettingsFlow.collect { settings ->
                loadAnime(settings.searchQuery, settings.status)
            }
        }
    }

    private fun loadAnime(searchQuery: String, status: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            getAnimeListUseCase(searchQuery, status)
                .catch { error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
                }
                .collect { list ->
                    _uiState.update { it.copy(isLoading = false, animeList = list) }
                }
        }
    }
}