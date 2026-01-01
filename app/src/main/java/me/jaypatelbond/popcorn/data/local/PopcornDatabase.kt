package me.jaypatelbond.popcorn.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import me.jaypatelbond.popcorn.data.local.dao.MovieDao
import me.jaypatelbond.popcorn.data.local.entity.MovieEntity

/**
 * Room database for the Popcorn app.
 */
@Database(
    entities = [MovieEntity::class],
    version = 1,
    exportSchema = false
)
abstract class PopcornDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}
