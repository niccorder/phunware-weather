package me.niccorder.phunware.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import me.niccorder.phunware.data.model.Location

/**
 * This is the definition of the [RoomDatabase] which generates all boilerplate code for our app's
 * local Sqlite3 database.
 *
 * @see LocationDao
 * @see android.arch.persistence.room
 */
@Database(
        entities = arrayOf(Location::class),
        version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao
}