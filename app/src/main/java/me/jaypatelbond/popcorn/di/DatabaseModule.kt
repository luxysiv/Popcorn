package me.jaypatelbond.popcorn.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.jaypatelbond.popcorn.data.local.PopcornDatabase
import me.jaypatelbond.popcorn.data.local.dao.MovieDao
import javax.inject.Singleton

/**
 * Hilt module providing database-related dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): PopcornDatabase {
        return Room.databaseBuilder(
            context,
            PopcornDatabase::class.java,
            "popcorn_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideMovieDao(database: PopcornDatabase): MovieDao {
        return database.movieDao()
    }
}
