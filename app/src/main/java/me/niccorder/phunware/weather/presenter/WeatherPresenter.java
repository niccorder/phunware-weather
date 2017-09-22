package me.niccorder.phunware.weather.presenter;

import io.reactivex.annotations.NonNull;
import me.niccorder.phunware.data.model.Location;

/**
 * Presents the weather information for a given {@link Location}.
 *
 * @see me.niccorder.phunware.weather.view.WeatherView
 */
public interface WeatherPresenter {

  /**
   * Set the current {@link Location} to present the weather for.
   */
  void setLocation(@NonNull final Location location);

  /**
   * @return the currently presented {@link Location}
   */
  @NonNull
  Location getLocation();

  /**
   * To be called when there is a need to load the weather for a {@link Location}.
   */
  void onLoadWeather();
}
