package me.jaypatelbond.popcorn.presentation.navigation

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import me.jaypatelbond.popcorn.presentation.details.MovieDetailsScreen
import me.jaypatelbond.popcorn.presentation.home.HomeScreen
import me.jaypatelbond.popcorn.presentation.saved.SavedMoviesScreen
import me.jaypatelbond.popcorn.presentation.search.SearchScreen

/**
 * Main navigation graph for the app.
 */
@Composable
fun PopcornNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        // Home Screen
        composable(route = Screen.Home.route) {
            HomeScreen(
                onMovieClick = { movie ->
                    navController.navigate(Screen.MovieDetails.createRoute(movie.id))
                }
            )
        }

        // Search Screen
        composable(route = Screen.Search.route) {
            SearchScreen(
                onMovieClick = { movie ->
                    navController.navigate(Screen.MovieDetails.createRoute(movie.id))
                }
            )
        }

        // Saved Movies Screen
        composable(route = Screen.Saved.route) {
            SavedMoviesScreen(
                onMovieClick = { movie ->
                    navController.navigate(Screen.MovieDetails.createRoute(movie.id))
                }
            )
        }

        // Movie Details Screen
        composable(
            route = Screen.MovieDetails.route,
            arguments = listOf(
                navArgument("movieId") { type = NavType.IntType }
            )
        ) {
            MovieDetailsScreen(
                onBackClick = { navController.popBackStack() },
                onShareClick = { movie ->
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_SUBJECT, movie.title)
                        putExtra(
                            Intent.EXTRA_TEXT,
                            "Check out ${movie.title} on Popcorn!\n\npopcorn://movie/${movie.id}"
                        )
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share movie"))
                }
            )
        }
    }
}
