package me.niccorder.phunware.data

import io.reactivex.Observable
import io.reactivex.annotations.NonNull
import me.niccorder.phunware.model.Forecast
import me.niccorder.phunware.model.Location

/**
 * A repository contract for interacting with all information for weather.
 */
interface WeatherRepository {

  /**
   * @param location of the forcast to be retrieved.
   * @return an [Observable] which emits the [Forecast] for the given [Location].
   */
  fun getForecast(@NonNull location: Location): Observable<Forecast>
}
