package me.jaypatelbond.popcorn.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.jaypatelbond.popcorn.domain.model.Movie
import me.jaypatelbond.popcorn.presentation.components.FeaturedMovieCard
import me.jaypatelbond.popcorn.presentation.components.FeaturedMovieShimmer
import me.jaypatelbond.popcorn.presentation.components.MovieCarousel
import me.jaypatelbond.popcorn.presentation.components.MovieCarouselShimmer
import me.jaypatelbond.popcorn.ui.theme.BackgroundDark
import me.jaypatelbond.popcorn.ui.theme.NetflixRed

/**
 * Home screen displaying trending and now playing movies.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onMovieClick: (Movie) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "🍿 Popcorn",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = NetflixRed
                    )
                },
                actions = {
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundDark
                )
            )
        },
        containerColor = BackgroundDark
    ) { paddingValues ->
        HomeContent(
            uiState = uiState,
            onMovieClick = onMovieClick,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun HomeContent(
    uiState: HomeUiState,
    onMovieClick: (Movie) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Featured Movie (first trending movie)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            if (uiState.isTrendingLoading && uiState.trendingMovies.isEmpty()) {
                FeaturedMovieShimmer()
            } else {
                uiState.trendingMovies.firstOrNull()?.let { featuredMovie ->
                    FeaturedMovieCard(
                        movie = featuredMovie,
                        onClick = { onMovieClick(featuredMovie) }
                    )
                }
            }
        }

        // Trending Movies Section
        if (uiState.isTrendingLoading && uiState.trendingMovies.isEmpty()) {
            MovieCarouselShimmer()
        } else if (uiState.trendingMovies.isNotEmpty()) {
            MovieCarousel(
                title = "🔥 Trending This Week",
                movies = uiState.trendingMovies,
                onMovieClick = onMovieClick
            )
        } else if (uiState.trendingError != null) {
            ErrorSection(
                message = "Failed to load trending movies",
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        // Now Playing Movies Section
        if (uiState.isNowPlayingLoading && uiState.nowPlayingMovies.isEmpty()) {
            MovieCarouselShimmer()
        } else if (uiState.nowPlayingMovies.isNotEmpty()) {
            MovieCarousel(
                title = "🎬 Now Playing",
                movies = uiState.nowPlayingMovies,
                onMovieClick = onMovieClick
            )
        } else if (uiState.nowPlayingError != null) {
            ErrorSection(
                message = "Failed to load now playing movies",
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        // Popular Movies (second half of trending)
        if (uiState.trendingMovies.size > 10) {
            MovieCarousel(
                title = "⭐ Popular Movies",
                movies = uiState.trendingMovies.drop(10),
                onMovieClick = onMovieClick
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun ErrorSection(
    message: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error
        )
    }
}
