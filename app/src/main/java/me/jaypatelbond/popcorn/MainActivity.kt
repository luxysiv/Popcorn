package me.jaypatelbond.popcorn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import me.jaypatelbond.popcorn.presentation.navigation.PopcornNavHost
import me.jaypatelbond.popcorn.ui.theme.PopcornTheme

/**
 * Main entry point of the Popcorn app.
 * Uses Hilt for dependency injection and Compose Navigation.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            PopcornTheme {
                val navController = rememberNavController()
                PopcornNavHost(navController = navController)
            }
        }
    }
}