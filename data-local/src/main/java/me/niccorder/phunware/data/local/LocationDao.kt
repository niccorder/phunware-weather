package me.niccorder.phunware.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import me.niccorder.phunware.model.Location

/**
 * The definition for our Data Access Object (DAO) which generates the CRUD code for mutating a
 * location object in our database.
 */
@Dao
interface LocationDao {

    /**
     * @return all cities stored in the local database.
     */
    @Query("SELECT * FROM location")
    fun getLocations(): Flow<List<Location>>

    @Query("SELECT * FROM location WHERE zipCode = :zipCode LIMIT 1")
    fun getLocations(zipCode: String): Flow<Location>

    /**
     * @return all cities stored in the local database.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(location: Location)
}