package me.jaypatelbond.popcorn.domain.model

/**
 * Domain model representing a movie.
 * This is the clean model used in the UI layer, decoupled from API and database models.
 */
data class Movie(
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
    val runtime: Int? = null,
    val tagline: String? = null,
    val genres: List<String> = emptyList()
) {
    /**
     * Returns the full poster image URL.
     */
    fun getPosterUrl(size: String = "w500"): String? {
        return posterPath?.let { "https://image.tmdb.org/t/p/$size$it" }
    }

    /**
     * Returns the full backdrop image URL.
     */
    fun getBackdropUrl(size: String = "w780"): String? {
        return backdropPath?.let { "https://image.tmdb.org/t/p/$size$it" }
    }

    /**
     * Returns the year from the release date.
     */
    fun getReleaseYear(): String? {
        return releaseDate?.take(4)
    }

    /**
     * Returns formatted vote average (e.g., "7.5").
     */
    fun getFormattedRating(): String {
        return String.format("%.1f", voteAverage)
    }

    /**
     * Returns formatted runtime (e.g., "2h 15m").
     */
    fun getFormattedRuntime(): String? {
        return runtime?.let {
            val hours = it / 60
            val minutes = it % 60
            if (hours > 0) "${hours}h ${minutes}m" else "${minutes}m"
        }
    }
}
