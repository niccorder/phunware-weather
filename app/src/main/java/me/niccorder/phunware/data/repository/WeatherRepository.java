package me.niccorder.phunware.data.repository;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import me.niccorder.phunware.model.Forecast;
import me.niccorder.phunware.model.Location;

/**
 * A repository contract for interacting with all information for weather.
 */
public interface WeatherRepository {

  /**
   * @param location of the forcast to be retrieved.
   * @return an {@link Observable} which emits the {@link Forecast} for the given {@link Location}.
   */
  Observable<Forecast> getForecast(@NonNull final Location location);
}
