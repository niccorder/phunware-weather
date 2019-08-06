package me.niccorder.phunware.model

enum class ForecastIcon(val type: String) {
  CLEAR_DAY("clear-day"),
  CLEAR_NIGHT("clear-night"),
  RAIN("rain"),
  SNOW("snow"),
  SLEET("sleet"),
  WIND("wind"),
  FOG("fog"),
  CLOUDY("cloudy"),
  PARTLY_CLOUDY_DAY("partly-cloudy-day"),
  PARTLY_CLOUDY_NIGHT("partly-cloudy-night"),
  UNKNOWN("unknown")
}