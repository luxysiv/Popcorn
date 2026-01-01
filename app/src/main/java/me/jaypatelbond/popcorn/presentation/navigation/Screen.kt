package me.jaypatelbond.popcorn.presentation.navigation

/**
 * Navigation routes for the app.
 */
sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object MovieDetails : Screen("movie/{movieId}") {
        fun createRoute(movieId: Int) = "movie/$movieId"
    }
    data object Search : Screen("search")
    data object Saved : Screen("saved")
}
