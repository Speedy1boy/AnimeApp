package com.krivykh.animeapp.presentation.anime_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krivykh.animeapp.data.repository.MockAnimeRepository
import com.krivykh.animeapp.domain.model.Anime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AnimeListUiState(
    val isLoading: Boolean = true,
    val animeList: List<Anime> = emptyList(),
    val errorMessage: String? = null
)

class AnimeListViewModel : ViewModel() {
    private val repository = MockAnimeRepository()

    private val _uiState = MutableStateFlow(AnimeListUiState())
    val uiState: StateFlow<AnimeListUiState> = _uiState.asStateFlow()

    init {
        loadAnime()
    }

    private fun loadAnime() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            runCatching {
                repository.getAnimeList()
            }.onSuccess { list ->
                _uiState.update { it.copy(isLoading = false, animeList = list) }
            }.onFailure { error ->
                _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
            }
        }
    }
}