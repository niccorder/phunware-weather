package me.niccorder.phunware.feature.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import me.niccorder.phunware.data.LocationRepository
import me.niccorder.phunware.model.Location
import timber.log.Timber
import javax.inject.Inject

class LocationListViewModel @Inject constructor(
  private val locationRepository: LocationRepository
) : ViewModel() {
  private val logger get() = Timber.tag("location-list-vm")
  private val disposables = CompositeDisposable()

  private val _locations = BehaviorSubject.createDefault<List<Location>>(emptyList())
  val locations: Observable<List<Location>> = _locations.observeOn(AndroidSchedulers.mainThread())

  init {
    viewModelScope.launch {
      locationRepository.locations.collect { locations ->
        _locations.onNext(locations)
      }
    }
  }

  fun addLocation(zipCode: String) {
    if (zipCode.length != 5) {
      Timber.e("Zip code must be 5 digits in length")
      return
    }

    val zipCodeInt = zipCode.toIntOrNull()
    when {
      zipCodeInt == null -> logger.e("Can not convert string zip code to integer")
      zipCodeInt !in 9999..100000 -> logger.e("Zip code is not within bounds.")
      else -> viewModelScope.launch {
        locationRepository.addLocation(zipCode)
      }
    }
  }

  override fun onCleared() {
    super.onCleared()
    logger.i("onCleared()")
    disposables.clear()
  }
}