package me.niccorder.phunware.weather.view;


import androidx.annotation.Nullable;

import io.reactivex.annotations.NonNull;

/**
 * Our contract for displaying weather information for a location. This should be implemented by an
 * Android component, and shouldn't have much more use.
 */
public interface WeatherView {

  /**
   * Displays the current temperature of the location.
   *
   * @param temperature for the current location.
   */
  void setTemperature(final double temperature);

  /**
   * Displays the current humidity value.
   *
   * @param humidity for the current location.
   */
  void setHumidity(@NonNull final double humidity);

  /**
   * Displays the summary of the weather.
   *
   * @param summary of the current location's weather.
   */
  void setSummary(@Nullable final String summary);

  /**
   * Displays a user friendly error if we run into any issues.
   *
   * @param throwable error that was raised.
   */
  void showError(@NonNull final Throwable throwable);
}
