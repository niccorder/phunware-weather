package me.niccorder.phunware.data.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Flowable
import me.niccorder.phunware.data.model.Location

/**
 * The definition for our Data Access Object (DAO) which generates the CRUD code for mutating a
 * location object in our database.
 *
 * @see android.arch.persistence.room.Dao
 */
@Dao
interface LocationDao {

    /**
     * @return all cities stored in the local database.
     */
    @Query("SELECT * FROM location")
    fun getLocations(): Flowable<List<Location>>

    /**
     * @return all cities stored in the local database.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocation(location: Location): Unit
}