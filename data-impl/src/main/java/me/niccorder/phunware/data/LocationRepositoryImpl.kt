package me.niccorder.phunware.data

import android.location.Geocoder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import me.niccorder.phunware.data.errors.UnauthorizedException
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

  private suspend fun getStartingLocations() {
    locationDao.insertLocation(
      Location(
        "91773",
        "San Dimas",
        34.110183,
        -117.810436
      )
    )
    locationDao.insertLocation(
      Location(
        "90401",
        "Santa Monica",
        34.013639,
        -118.493974
      )
    )
    locationDao.insertLocation(
      Location(
        "10005",
        "New York",
        40.706015,
        -74.008959
      )
    )
  }

  override val locations: Flow<List<Location>> = locationDao.getLocations()

  override suspend fun getLocation(zipCode: String): Location {
    val response = locationApi.getLocationInfo(
      BuildConfig.LOCATION_API_KEY,
      zipCode
    )

    if (response.isSuccessful) return response.body()!!

    when (response.code()) {
      401 -> throw UnauthorizedException
      else -> throw UnknownError("Unknown error occurred when loading information for zipCode: $zipCode")
    }
  }

  override fun observeLocation(zipCode: String): Flow<Location>
      = locationDao.getLocations(zipCode).onStart { emit(Location.EMPTY) }

  override suspend fun addLocation(zipCode: String): Location {
    val location = getLocation(zipCode)
    logger.i("Inserting location ${location.zipCode}")
    locationDao.insertLocation(location)
    return location
  }
}