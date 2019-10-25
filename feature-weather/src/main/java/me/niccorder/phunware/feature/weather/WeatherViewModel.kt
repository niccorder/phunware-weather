package me.niccorder.phunware.feature.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.launch
import me.niccorder.phunware.data.LocationRepository
import me.niccorder.phunware.data.WeatherRepository
import me.niccorder.phunware.model.Forecast
import me.niccorder.phunware.model.Location
import timber.log.Timber
import javax.inject.Inject
import kotlin.properties.Delegates.observable

class WeatherViewModel @Inject constructor(
  private val locationRepository: LocationRepository,
  private val weatherRepository: WeatherRepository
) : ViewModel() {
  private val logger get() = Timber.tag("weather-vm")
  private val disposables = CompositeDisposable()

  private val _location = BehaviorSubject.create<Location>()
  val location: Observable<Location> = _location.observeOn(AndroidSchedulers.mainThread())

  private val _forecast = BehaviorSubject.create<Forecast>()
  val forecast: Observable<Forecast> = _forecast.observeOn(AndroidSchedulers.mainThread())

  var zipCode: String? by observable(null as String?) { _, old, new ->
    logger.i("Setting zip code: $zipCode")

    if (new != null) loadInformation(new)
  }

  private fun loadInformation(zipCode: String) {
    logger.i("Loading weather information.")
    viewModelScope.launch {
      val location = locationRepository.getLocation(zipCode)
      _location.onNext(location)
      val forecast = weatherRepository.getForecast(location)
      _forecast.onNext(forecast)
    }
  }

  override fun onCleared() {
    super.onCleared()
    logger.i("onCleared()")
    disposables.clear()
  }
}