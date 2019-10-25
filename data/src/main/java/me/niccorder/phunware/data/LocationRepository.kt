package me.niccorder.phunware.data

import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
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
  val locations: Observable<List<Location>>

  /**
   * Converts a zip-code to a [Location].
   *
   * @param zipCode a 5 digit-only string zip code.
   * @return a [Flowable] which emits the Location zip equivalent.
   */
  fun getLocation(zipCode: String): Single<Location>

  /**
   * Converts a zip-code to a [Location].
   *
   * @param zipCode a 5 digit-only string zip code.
   * @return a [Flowable] which emits the Location zip equivalent.
   */
  fun observeLocation(zipCode: String): Observable<Location>

  /**
   * Add's a location to the repository.
   *
   * @param location to be added.
   * @return a [Single] which emit's the original location once it has been added.
   */
  fun addLocation(zipCode: String): Single<Location>
}
