package me.niccorder.phunware.weather

import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import me.niccorder.phunware.data.LocationRepository
import me.niccorder.phunware.data.WeatherRepository
import me.niccorder.phunware.internal.addTo
import me.niccorder.phunware.model.Forecast
import me.niccorder.phunware.model.Location
import timber.log.Timber
import javax.inject.Inject

class WeatherViewModel @Inject constructor(
  private val locationRepository: LocationRepository,
  private val weatherRepository: WeatherRepository
) : ViewModel() {
  private val logger get() = Timber.tag("weather-vm")
  private val disposables = CompositeDisposable()

  private val zipCodeSubject: BehaviorSubject<String> = BehaviorSubject.createDefault("")

  fun setZipCode(zipCode: String) {
    logger.i("Setting zip code: $zipCode")
    zipCodeSubject.onNext(zipCode)
  }

  private val _location = BehaviorSubject.create<Location>()
  val location: Observable<Location> = _location.observeOn(AndroidSchedulers.mainThread())

  private val _forecast = BehaviorSubject.create<Forecast>()
  val forecast: Observable<Forecast> = _forecast.observeOn(AndroidSchedulers.mainThread())

  init {
    zipCodeSubject.switchMap { zipCode -> locationRepository.observeLocation(zipCode) }
      .observeOn(Schedulers.io())
      .subscribeBy(
        onNext = { location ->
          logger.i("Successfully loaded location")
          _location.onNext(location)
        },
        onError = { throwable ->
          logger.e(throwable, "Error while loading location.")
        }
      ) addTo disposables

    _location.switchMap { location -> weatherRepository.getForecast(location) }
      .subscribeBy(
        onNext = { forecast ->
          logger.i("Successfully loaded forecast.")
          _forecast.onNext(forecast)
        },
        onError = { throwable ->
          logger.e(throwable, "Error while loading forecast")
        }
      ) addTo disposables
  }

  override fun onCleared() {
    super.onCleared()
    logger.i("onCleared()")
    disposables.clear()
  }
}