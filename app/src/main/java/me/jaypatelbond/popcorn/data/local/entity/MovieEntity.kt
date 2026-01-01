package me.jaypatelbond.popcorn.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity representing a movie stored in the local database.
 */
@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val releaseDate: String?,
    val voteAverage: Double,
    val voteCount: Int,
    val popularity: Double,
    val isBookmarked: Boolean = false,
    val movieType: String, // "trending" or "now_playing"
    val runtime: Int? = null,
    val tagline: String? = null,
    val genres: String? = null, // Stored as comma-separated string
    val lastUpdated: Long = System.currentTimeMillis()
)
