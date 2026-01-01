package me.jaypatelbond.popcorn.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import me.jaypatelbond.popcorn.data.local.entity.MovieEntity

/**
 * Data Access Object for movie database operations.
 */
@Dao
interface MovieDao {

    /**
     * Get all movies of a specific type (trending/now_playing) as a Flow.
     */
    @Query("SELECT * FROM movies WHERE movieType = :type ORDER BY popularity DESC")
    fun getMoviesByType(type: String): Flow<List<MovieEntity>>

    /**
     * Get all bookmarked movies as a Flow.
     */
    @Query("SELECT * FROM movies WHERE isBookmarked = 1 ORDER BY lastUpdated DESC")
    fun getBookmarkedMovies(): Flow<List<MovieEntity>>

    /**
     * Get a single movie by ID.
     */
    @Query("SELECT * FROM movies WHERE id = :movieId")
    suspend fun getMovieById(movieId: Int): MovieEntity?

    /**
     * Get a single movie by ID as a Flow.
     */
    @Query("SELECT * FROM movies WHERE id = :movieId")
    fun getMovieByIdFlow(movieId: Int): Flow<MovieEntity?>

    /**
     * Insert or replace a list of movies.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)

    /**
     * Insert or replace a single movie.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieEntity)

    /**
     * Update an existing movie.
     */
    @Update
    suspend fun updateMovie(movie: MovieEntity)

    /**
     * Toggle bookmark status for a movie.
     */
    @Query("UPDATE movies SET isBookmarked = NOT isBookmarked WHERE id = :movieId")
    suspend fun toggleBookmark(movieId: Int)

    /**
     * Search movies by title.
     */
    @Query("SELECT * FROM movies WHERE title LIKE '%' || :query || '%' ORDER BY popularity DESC")
    fun searchMovies(query: String): Flow<List<MovieEntity>>

    /**
     * Delete all movies of a specific type (for refreshing).
     */
    @Query("DELETE FROM movies WHERE movieType = :type AND isBookmarked = 0")
    suspend fun deleteMoviesByType(type: String)

    /**
     * Get the count of movies by type.
     */
    @Query("SELECT COUNT(*) FROM movies WHERE movieType = :type")
    suspend fun getMovieCountByType(type: String): Int
}
