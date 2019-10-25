package me.niccorder.phunware.data

import android.app.ActivityManager
import android.util.LruCache
import me.niccorder.phunware.data.errors.UnauthorizedException
import me.niccorder.phunware.data.remote.WeatherApi
import me.niccorder.phunware.model.Forecast
import me.niccorder.phunware.model.Location
import me.niccorder.scopes.AppScope
import javax.inject.Inject

/**
 * @see WeatherRepository
 */
@AppScope
class WeatherRepositoryImpl @Inject constructor(
  private val weatherApi: WeatherApi,
  private val memoryInfo: ActivityManager.MemoryInfo
) : WeatherRepository {

  private val forecastCache: LruCache<String, Forecast> = LruCache(
    ((memoryInfo.availMem / 0x100000L) / 100).toInt()       // 1% of Available memory
  )

  override suspend fun getForecast(location: Location): Forecast {
    var forecast = forecastCache.get(location.zipCode)
    if (forecast != null) return forecast

    val response = weatherApi.getForcast(
      BuildConfig.WEATHER_API_KEY,
      location.latitude,
      location.longitude
    )

    if (response.isSuccessful) {
      forecast = response.body()!!
      forecastCache.put(location.zipCode, forecast)
      return forecast
    }

    when (response.code()) {
      401 -> throw UnauthorizedException
      else -> throw UnknownError("An unknown error occurred when attempting to fetch forecast")
    }
  }
}