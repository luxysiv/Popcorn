package me.jaypatelbond.popcorn.presentation.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.jaypatelbond.popcorn.domain.repository.MovieRepository
import javax.inject.Inject

/**
 * ViewModel for the Saved Movies screen.
 */
@HiltViewModel
class SavedMoviesViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SavedMoviesUiState())
    val uiState: StateFlow<SavedMoviesUiState> = _uiState.asStateFlow()

    init {
        loadBookmarkedMovies()
    }

    private fun loadBookmarkedMovies() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            movieRepository.getBookmarkedMovies().collect { movies ->
                _uiState.update { 
                    it.copy(
                        movies = movies,
                        isLoading = false
                    )
                }
            }
        }
    }
}
