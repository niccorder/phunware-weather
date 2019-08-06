package me.niccorder.phunware.data.repository

import android.location.Address
import android.location.Geocoder
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import me.niccorder.phunware.data.local.LocationDao
import me.niccorder.phunware.data.remote.api.LocationApi
import me.niccorder.scopes.AppScope
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

@me.niccorder.scopes.AppScope
class LocationRepositoryImpl @Inject constructor(
        private val geocoder: Geocoder,
        private val locationDao: LocationDao,
        private val locationApi: LocationApi
) : LocationRepository {

    private fun getStartingLocations(): Flowable<MutableList<me.niccorder.phunware.model.Location>> = Flowable.just(
            mutableListOf(
                me.niccorder.phunware.model.Location(
                    "91773",
                    "San Dimas",
                    34.110183,
                    -117.810436
                ),
                me.niccorder.phunware.model.Location(
                    "90401",
                    "Santa Monica",
                    34.013639,
                    -118.493974
                ),
                me.niccorder.phunware.model.Location(
                    "10005",
                    "New York",
                    40.706015,
                    -74.008959
                )
            )
    ).doOnNext { it.forEach { locationDao.insertLocation(it) } }.subscribeOn(Schedulers.io())

    override fun getLocations(): Flowable<MutableList<me.niccorder.phunware.model.Location>> = locationDao.getLocations()
            .map { it.toMutableList() }
            .flatMap {
                if (it.isEmpty()) {
                    return@flatMap getStartingLocations()
                }
                return@flatMap Flowable.just(it)
            }
            .subscribeOn(Schedulers.io())

    override fun addLocation(location: me.niccorder.phunware.model.Location): Single<me.niccorder.phunware.model.Location> = Single.create<me.niccorder.phunware.model.Location> {
        locationDao.insertLocation(location)
        it.onSuccess(location)
    }

    override fun getLocation(zipCode: String): Flowable<me.niccorder.phunware.model.Location> = Flowable.zip(
            locationApi.getLocationInfo(zipCode).onErrorReturn { me.niccorder.phunware.model.Location.EMPTY },
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
                me.niccorder.phunware.model.Location(
                    zipCode,
                    it.featureName,
                    it.latitude,
                    it.longitude
                )
            }.onErrorReturn { me.niccorder.phunware.model.Location.EMPTY },
            BiFunction { remote: me.niccorder.phunware.model.Location, local: me.niccorder.phunware.model.Location ->
                Timber.e(remote.toString())
                Timber.e(local.toString())
                local.copy(city = remote.city)
            })
            .doOnNext { if (me.niccorder.phunware.model.Location.EMPTY == it) throw IOException() }
            .subscribeOn(Schedulers.io())
}