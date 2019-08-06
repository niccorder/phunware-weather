package me.niccorder.phunware.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import me.niccorder.phunware.model.Location

/**
 * This is the definition of the [RoomDatabase] which generates all boilerplate code for our app's
 * local Sqlite3 database.
 *
 * @see LocationDao
 * @see android.arch.persistence.room
 */
@Database(
        entities = [Location::class],
        version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao
}