package me.niccorder.phunware.location.data

import android.location.Address
import android.location.Geocoder
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import me.niccorder.phunware.data.local.LocationDao
import me.niccorder.phunware.data.model.Location
import me.niccorder.phunware.data.remote.api.LocationApi
import me.niccorder.phunware.internal.AppScope
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

@AppScope
class LocationRepositoryImpl @Inject constructor(
        private val geocoder: Geocoder,
        private val locationDao: LocationDao,
        private val locationApi: LocationApi
) : LocationRepository {

    private fun getStartingLocations(): Flowable<MutableList<Location>> = Flowable.just(
            mutableListOf(
                    Location(
                            "91773",
                            "San Dimas",
                            34.110183,
                            -117.810436
                    ),
                    Location(
                            "90401",
                            "Santa Monica",
                            34.013639,
                            -118.493974
                    ),
                    Location(
                            "10005",
                            "New York",
                            40.706015,
                            -74.008959
                    )
            )
    ).doOnNext { it.forEach { locationDao.insertLocation(it) } }.subscribeOn(Schedulers.io())

    override fun getLocations(): Flowable<MutableList<Location>> = locationDao.getLocations()
            .map { it.toMutableList() }
            .flatMap {
                if (it.isEmpty()) {
                    return@flatMap getStartingLocations()
                }
                return@flatMap Flowable.just(it)
            }
            .subscribeOn(Schedulers.io())

    override fun addLocation(location: Location): Single<Location> = Single.create<Location> {
        locationDao.insertLocation(location)
        it.onSuccess(location)
    }

    override fun getLocation(zipCode: String): Flowable<Location> = Flowable.zip(
            locationApi.getLocationInfo(zipCode).onErrorReturn { Location.EMPTY },
            Flowable.create<Address>(
                    {
                        try {
                            it.onNext(geocoder.getFromLocationName(zipCode, 1)[0])
                        } catch (t: Throwable) {
                            it.onError(t)
                        }
                    },
                    BackpressureStrategy.LATEST
            ).map {
                Location(
                        zipCode,
                        it.featureName,
                        it.latitude,
                        it.longitude
                )
            }.onErrorReturn { Location.EMPTY },
            BiFunction { remote: Location, local: Location ->
                Timber.e(remote.toString())
                Timber.e(local.toString())
                local.copy(city = remote.city)
            })
            .doOnNext { if (Location.EMPTY == it) throw IOException() }
            .subscribeOn(Schedulers.io())
}