package me.jaypatelbond.popcorn.domain.repository

import kotlinx.coroutines.flow.Flow
import me.jaypatelbond.popcorn.domain.model.Movie
import me.jaypatelbond.popcorn.util.Resource

/**
 * Repository interface for movie data operations.
 * Follows the Repository pattern for clean architecture.
 */
interface MovieRepository {

    /**
     * Get trending movies with offline-first strategy.
     * Returns cached data immediately and fetches fresh data in background.
     */
    fun getTrendingMovies(): Flow<Resource<List<Movie>>>

    /**
     * Get now playing movies with offline-first strategy.
     */
    fun getNowPlayingMovies(): Flow<Resource<List<Movie>>>

    /**
     * Get detailed information about a specific movie.
     */
    fun getMovieDetails(movieId: Int): Flow<Resource<Movie>>

    /**
     * Search for movies by query.
     */
    fun searchMovies(query: String): Flow<Resource<List<Movie>>>

    /**
     * Get all bookmarked movies.
     */
    fun getBookmarkedMovies(): Flow<List<Movie>>

    /**
     * Toggle bookmark status for a movie.
     */
    suspend fun toggleBookmark(movieId: Int)

    /**
     * Check if a movie is bookmarked.
     */
    suspend fun isMovieBookmarked(movieId: Int): Boolean
}
