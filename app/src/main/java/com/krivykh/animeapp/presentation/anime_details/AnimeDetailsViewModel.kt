package com.krivykh.animeapp.presentation.anime_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krivykh.animeapp.di.AppModule
import com.krivykh.animeapp.domain.model.Anime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AnimeDetailsUiState(
    val isLoading: Boolean = true,
    val anime: Anime? = null,
    val errorMessage: String? = null
)

class AnimeDetailsViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val getAnimeDetailsUseCase = AppModule.getAnimeDetailsUseCase
    private val repository = AppModule.repository
    private val animeId: String = checkNotNull(savedStateHandle["animeId"])

    private val _uiState = MutableStateFlow(AnimeDetailsUiState())
    val uiState: StateFlow<AnimeDetailsUiState> = _uiState.asStateFlow()

    val isFavorite: StateFlow<Boolean> = repository.isFavorite(animeId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = false
        )

    init {
        loadAnimeDetails()
    }

    private fun loadAnimeDetails() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            runCatching {
                getAnimeDetailsUseCase(animeId)
            }.onSuccess { result ->
                _uiState.update { it.copy(isLoading = false, anime = result) }
            }.onFailure { error ->
                _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
            }
        }
    }

    fun toggleFavorite() {
        val anime = _uiState.value.anime ?: return
        viewModelScope.launch {
            if (isFavorite.value) {
                repository.removeFavorite(anime)
            } else {
                repository.addFavorite(anime)
            }
        }
    }
}