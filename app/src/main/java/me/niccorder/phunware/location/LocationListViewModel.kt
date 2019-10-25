package me.niccorder.phunware.location

import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import me.niccorder.phunware.data.LocationRepository
import me.niccorder.phunware.model.Location
import me.niccorder.util.rx.addTo
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
    locationRepository.locations
      .observeOn(Schedulers.io())
      .subscribeBy(
        onNext = {
          logger.i("Received more locations.")
          _locations.onNext(it)
        },
        onError = { throwable ->
          logger.e(throwable, "Error while observing locations")
        }
      ) addTo disposables
  }

  fun addLocation(zipCode: String) {
    if (zipCode.length != 5) {
      Timber.e("Zip code must be 5 digits in length")
      return
    }

    val zipCodeInt = zipCode.toIntOrNull()
    when {
      zipCodeInt == null -> Timber.e("Can not convert string zip code to integer")
      zipCodeInt !in 9999..100000 -> Timber.e("Zip code is not within bounds.")
      else ->
        locationRepository.addLocation(zipCode)
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(
            { },
            {
              // view.showError(it)
            }
          )
    }
  }

  override fun onCleared() {
    super.onCleared()
    logger.i("onCleared()")
    disposables.clear()
  }
}