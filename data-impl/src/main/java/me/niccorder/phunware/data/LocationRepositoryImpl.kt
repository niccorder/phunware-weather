package me.niccorder.phunware.data

import android.location.Geocoder
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import me.niccorder.phunware.data.local.LocationDao
import me.niccorder.phunware.data.remote.LocationApi
import me.niccorder.phunware.model.Location
import me.niccorder.scopes.AppScope
import timber.log.Timber
import javax.inject.Inject

@AppScope
class LocationRepositoryImpl @Inject constructor(
  private val geocoder: Geocoder,
  private val locationDao: LocationDao,
  private val locationApi: LocationApi
) : LocationRepository {
  private val logger get() = Timber.tag("location-repo")

  init {
    getStartingLocations().subscribe {
      logger.i("Successfully inserted starting locations")
    }
  }

  private fun getStartingLocations(): Flowable<List<Location>> =
    Flowable.just(
      listOf(
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

  override val locations: Observable<List<Location>> = locationDao.getLocations()
    .observeOn(Schedulers.io())

  override fun getLocation(zipCode: String): Single<Location> = locationApi.getLocationInfo(
    BuildConfig.LOCATION_API_KEY,
    zipCode
  ).onErrorReturn { Location.EMPTY }

  override fun observeLocation(
    zipCode: String
  ): Observable<Location> = locationDao.getLocations(zipCode).map { locations ->
    if (locations.isNotEmpty()) locations.first() else Location.EMPTY
  }

  override fun addLocation(zipCode: String): Single<Location> = getLocation(zipCode)
    .doOnSuccess { location -> locationDao.insertLocation(location) }
}