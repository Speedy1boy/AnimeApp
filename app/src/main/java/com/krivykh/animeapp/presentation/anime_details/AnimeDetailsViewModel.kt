package com.krivykh.animeapp.presentation.anime_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krivykh.animeapp.data.repository.MockAnimeRepository
import com.krivykh.animeapp.domain.model.Anime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AnimeDetailsUiModel(
    val title: String,
    val posterImageUrl: String,
    val synopsis: String,
    val statusText: String,
    val infoText: String
)

data class AnimeDetailsUiState(
    val isLoading: Boolean = true,
    val animeDetails: AnimeDetailsUiModel? = null,
    val errorMessage: String? = null
)

private fun Anime.toUiModel(): AnimeDetailsUiModel {
    return AnimeDetailsUiModel(
        title = this.title,
        posterImageUrl = this.posterImageUrl,
        synopsis = this.synopsis,
        statusText = "Status: ${this.status}",
        infoText = "Episodes: ${this.episodeCount} | Rating: ${this.averageRating}"
    )
}

class AnimeDetailsViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val repository = MockAnimeRepository()
    private val animeId: String = checkNotNull(savedStateHandle["animeId"])

    private val _uiState = MutableStateFlow(AnimeDetailsUiState())
    val uiState: StateFlow<AnimeDetailsUiState> = _uiState.asStateFlow()

    init {
        loadAnimeDetails()
    }

    private fun loadAnimeDetails() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            runCatching {
                repository.getAnimeById(animeId)
            }.onSuccess { result ->
                _uiState.update { it.copy(isLoading = false, animeDetails = result.toUiModel()) }
            }.onFailure { error ->
                _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
            }
        }
    }
}