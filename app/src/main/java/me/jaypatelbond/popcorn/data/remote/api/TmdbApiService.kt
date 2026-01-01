package me.jaypatelbond.popcorn.data.remote.api

import me.jaypatelbond.popcorn.data.remote.dto.MovieDetailDto
import me.jaypatelbond.popcorn.data.remote.dto.MovieListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit service interface for TMDB API endpoints.
 */
interface TmdbApiService {

    /**
     * Get trending movies for the week.
     */
    @GET("trending/movie/week")
    suspend fun getTrendingMovies(
        @Query("page") page: Int = 1
    ): MovieListResponse

    /**
     * Get movies currently playing in theaters.
     */
    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("page") page: Int = 1
    ): MovieListResponse

    /**
     * Get detailed information about a specific movie.
     */
    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int
    ): MovieDetailDto

    /**
     * Search for movies by query string.
     */
    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int = 1
    ): MovieListResponse
}
