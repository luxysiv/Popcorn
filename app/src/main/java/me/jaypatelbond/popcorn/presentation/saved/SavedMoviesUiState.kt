package me.jaypatelbond.popcorn.presentation.saved

import me.jaypatelbond.popcorn.domain.model.Movie

/**
 * UI state for the Saved Movies screen.
 */
data class SavedMoviesUiState(
    val movies: List<Movie> = emptyList(),
    val isLoading: Boolean = false
)
