package me.niccorder.phunware.data

import kotlinx.coroutines.flow.Flow
import me.niccorder.phunware.model.Location

/**
 * Our data-layer contract which handles logic for displaying/retrieving information relating to a
 * location.
 *
 *
 * This allows us to easily keep the android/java barrier in place for unit testing (as well as
 * allows us easier unit testing by mocking).
 */
interface LocationRepository {

  /**
   * @return a [Flowable] which emits all currently stored locations.
   */
  val locations: Flow<List<Location>>

  /**
   * Converts a zip-code to a [Location].
   *
   * @param zipCode a 5 digit-only string zip code.
   * @return a [Flowable] which emits the Location zip equivalent.
   */
  suspend fun getLocation(zipCode: String): Location

  /**
   * Converts a zip-code to a [Location].
   *
   * @param zipCode a 5 digit-only string zip code.
   * @return a [Flowable] which emits the Location zip equivalent.
   */
  fun observeLocation(zipCode: String): Flow<Location>

  /**
   * Add's a location to the repository.
   *
   * @param location to be added.
   * @return a [Single] which emit's the original location once it has been added.
   */
  suspend fun addLocation(zipCode: String): Location
}
