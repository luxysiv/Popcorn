package me.jaypatelbond.popcorn.data.mapper

import me.jaypatelbond.popcorn.data.local.entity.MovieEntity
import me.jaypatelbond.popcorn.data.remote.dto.MovieDetailDto
import me.jaypatelbond.popcorn.data.remote.dto.MovieDto
import me.jaypatelbond.popcorn.domain.model.Movie

/**
 * Mapper functions to convert between data layer models and domain models.
 */

/**
 * Convert MovieDto (API) to MovieEntity (Database).
 */
fun MovieDto.toEntity(movieType: String, isBookmarked: Boolean = false): MovieEntity {
    return MovieEntity(
        id = id,
        title = title,
        overview = overview ?: "",
        posterPath = posterPath,
        backdropPath = backdropPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        voteCount = voteCount,
        popularity = popularity,
        isBookmarked = isBookmarked,
        movieType = movieType
    )
}

/**
 * Convert MovieDetailDto (API) to MovieEntity (Database).
 */
fun MovieDetailDto.toEntity(movieType: String = "detail", isBookmarked: Boolean = false): MovieEntity {
    return MovieEntity(
        id = id,
        title = title,
        overview = overview ?: "",
        posterPath = posterPath,
        backdropPath = backdropPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        voteCount = voteCount,
        popularity = popularity,
        isBookmarked = isBookmarked,
        movieType = movieType,
        runtime = runtime,
        tagline = tagline,
        genres = genres?.joinToString(",") { it.name }
    )
}

/**
 * Convert MovieEntity (Database) to Movie (Domain).
 */
fun MovieEntity.toDomain(): Movie {
    return Movie(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        voteCount = voteCount,
        popularity = popularity,
        isBookmarked = isBookmarked,
        runtime = runtime,
        tagline = tagline,
        genres = genres?.split(",")?.filter { it.isNotBlank() } ?: emptyList()
    )
}

/**
 * Convert MovieDto (API) directly to Movie (Domain).
 */
fun MovieDto.toDomain(): Movie {
    return Movie(
        id = id,
        title = title,
        overview = overview ?: "",
        posterPath = posterPath,
        backdropPath = backdropPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        voteCount = voteCount,
        popularity = popularity
    )
}

/**
 * Convert MovieDetailDto (API) directly to Movie (Domain).
 */
fun MovieDetailDto.toDomain(): Movie {
    return Movie(
        id = id,
        title = title,
        overview = overview ?: "",
        posterPath = posterPath,
        backdropPath = backdropPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        voteCount = voteCount,
        popularity = popularity,
        runtime = runtime,
        tagline = tagline,
        genres = genres?.map { it.name } ?: emptyList()
    )
}

/**
 * Convert list of MovieDto to list of MovieEntity.
 */
fun List<MovieDto>.toEntityList(movieType: String): List<MovieEntity> {
    return map { it.toEntity(movieType) }
}

/**
 * Convert list of MovieEntity to list of Movie.
 */
fun List<MovieEntity>.toDomainList(): List<Movie> {
    return map { it.toDomain() }
}
