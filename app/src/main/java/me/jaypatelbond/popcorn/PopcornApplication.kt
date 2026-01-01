package me.jaypatelbond.popcorn

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for Popcorn app.
 * Annotated with @HiltAndroidApp to enable Hilt dependency injection.
 */
@HiltAndroidApp
class PopcornApplication : Application()
