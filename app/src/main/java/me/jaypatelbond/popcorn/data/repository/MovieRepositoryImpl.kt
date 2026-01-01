package me.jaypatelbond.popcorn.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import me.jaypatelbond.popcorn.data.local.dao.MovieDao
import me.jaypatelbond.popcorn.data.mapper.toDomain
import me.jaypatelbond.popcorn.data.mapper.toDomainList
import me.jaypatelbond.popcorn.data.mapper.toEntity
import me.jaypatelbond.popcorn.data.mapper.toEntityList
import me.jaypatelbond.popcorn.data.remote.api.TmdbApiService
import me.jaypatelbond.popcorn.domain.model.Movie
import me.jaypatelbond.popcorn.domain.repository.MovieRepository
import me.jaypatelbond.popcorn.util.Resource
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of MovieRepository with offline-first strategy.
 * Data flows: API -> Database -> UI
 */
@Singleton
class MovieRepositoryImpl @Inject constructor(
    private val apiService: TmdbApiService,
    private val movieDao: MovieDao
) : MovieRepository {

    companion object {
        const val TYPE_TRENDING = "trending"
        const val TYPE_NOW_PLAYING = "now_playing"
    }

    override fun getTrendingMovies(): Flow<Resource<List<Movie>>> = flow {
        emit(Resource.Loading())

        // First, emit cached data if available
        val cachedMovies = movieDao.getMoviesByType(TYPE_TRENDING).first()
        if (cachedMovies.isNotEmpty()) {
            emit(Resource.Loading(cachedMovies.toDomainList()))
        }

        // Fetch fresh data from network
        try {
            val response = apiService.getTrendingMovies()
            val movies = response.results.toEntityList(TYPE_TRENDING)

            // Preserve bookmark status for existing movies
            val updatedMovies = movies.map { movie ->
                val existingMovie = movieDao.getMovieById(movie.id)
                movie.copy(isBookmarked = existingMovie?.isBookmarked ?: false)
            }

            movieDao.insertMovies(updatedMovies)
            emit(Resource.Success(updatedMovies.map { it.toDomain() }))
        } catch (e: Exception) {
            if (cachedMovies.isNotEmpty()) {
                emit(Resource.Error(e.message ?: "Unknown error", cachedMovies.toDomainList()))
            } else {
                emit(Resource.Error(e.message ?: "Unknown error"))
            }
        }
    }

    override fun getNowPlayingMovies(): Flow<Resource<List<Movie>>> = flow {
        emit(Resource.Loading())

        // First, emit cached data if available
        val cachedMovies = movieDao.getMoviesByType(TYPE_NOW_PLAYING).first()
        if (cachedMovies.isNotEmpty()) {
            emit(Resource.Loading(cachedMovies.toDomainList()))
        }

        try {
            // Fetch from network
            val response = apiService.getNowPlayingMovies()
            val movies = response.results.toEntityList(TYPE_NOW_PLAYING)

            // Preserve bookmark status
            val updatedMovies = movies.map { movie ->
                val existingMovie = movieDao.getMovieById(movie.id)
                movie.copy(isBookmarked = existingMovie?.isBookmarked ?: false)
            }

            movieDao.insertMovies(updatedMovies)
            emit(Resource.Success(updatedMovies.map { it.toDomain() }))
        } catch (e: Exception) {
            if (cachedMovies.isNotEmpty()) {
                emit(Resource.Error(e.message ?: "Unknown error", cachedMovies.toDomainList()))
            } else {
                emit(Resource.Error(e.message ?: "Unknown error"))
            }
        }
    }

    override fun getMovieDetails(movieId: Int): Flow<Resource<Movie>> = flow {
        emit(Resource.Loading())

        // First emit cached data if available
        val cachedMovie = movieDao.getMovieById(movieId)
        cachedMovie?.let {
            emit(Resource.Loading(it.toDomain()))
        }

        try {
            // Fetch fresh details from API
            val response = apiService.getMovieDetails(movieId)
            val isBookmarked = cachedMovie?.isBookmarked ?: false
            val movieEntity = response.toEntity(cachedMovie?.movieType ?: "detail", isBookmarked)

            movieDao.insertMovie(movieEntity)
            emit(Resource.Success(movieEntity.toDomain()))
        } catch (e: Exception) {
            if (cachedMovie != null) {
                emit(Resource.Error(e.message ?: "Unknown error", cachedMovie.toDomain()))
            } else {
                emit(Resource.Error(e.message ?: "Unknown error"))
            }
        }
    }

    override fun searchMovies(query: String): Flow<Resource<List<Movie>>> = flow {
        if (query.isBlank()) {
            emit(Resource.Success(emptyList()))
            return@flow
        }

        emit(Resource.Loading())

        try {
            val response = apiService.searchMovies(query)
            val movies = response.results.map { it.toDomain() }
            emit(Resource.Success(movies))
        } catch (e: Exception) {
            // Try local search as fallback
            val localResults = movieDao.searchMovies(query).first()
            if (localResults.isNotEmpty()) {
                emit(Resource.Error(e.message ?: "Unknown error", localResults.toDomainList()))
            } else {
                emit(Resource.Error(e.message ?: "Unknown error"))
            }
        }
    }

    override fun getBookmarkedMovies(): Flow<List<Movie>> {
        return movieDao.getBookmarkedMovies().map { entities ->
            entities.toDomainList()
        }
    }

    override suspend fun toggleBookmark(movieId: Int) {
        movieDao.toggleBookmark(movieId)
    }

    override suspend fun isMovieBookmarked(movieId: Int): Boolean {
        return movieDao.getMovieById(movieId)?.isBookmarked ?: false
    }
}
