package me.niccorder.phunware.data.repository

import android.app.ActivityManager
import android.util.LruCache
import io.reactivex.Observable
import io.reactivex.subjects.AsyncSubject
import me.niccorder.phunware.model.Forecast
import me.niccorder.phunware.model.Location
import me.niccorder.phunware.data.remote.api.WeatherApi
import me.niccorder.phunware.internal.AppScope
import javax.inject.Inject

/**
 * @see WeatherRepository
 */
@AppScope
class WeatherRepositoryImpl @Inject constructor(
        private val weatherApi: WeatherApi,
        private val memoryInfo: ActivityManager.MemoryInfo
) : WeatherRepository {

    private val forecastCache: LruCache<String, AsyncSubject<me.niccorder.phunware.model.Forecast>> = LruCache(
            ((memoryInfo.availMem / 0x100000L) / 100).toInt()       // 1% of Available memory
    )

    override fun getForecast(location: me.niccorder.phunware.model.Location): Observable<me.niccorder.phunware.model.Forecast> {
        var forecastSubject = forecastCache.get(location.zipCode)
        if (forecastSubject == null) {
            forecastSubject = AsyncSubject.create()

            weatherApi.getForcast(
                    location.latitude,
                    location.longitude
            ).subscribe(forecastSubject)
            forecastCache.put(location.zipCode, forecastSubject)
        } else {
            forecastSubject.doOnNext {
                if (System.currentTimeMillis() > it.current?.time ?: 0) {
                    forecastCache.remove(location.zipCode)
                }
            }
        }

        return forecastSubject
    }
}