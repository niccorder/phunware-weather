package me.niccorder.phunware.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Observable
import me.niccorder.phunware.model.Location

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
    fun getLocations(): Observable<List<Location>>

    @Query("SELECT * FROM location WHERE zipCode = :zipCode")
    fun getLocations(zipCode: String): Observable<List<Location>>

    /**
     * @return all cities stored in the local database.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocation(location: Location)
}