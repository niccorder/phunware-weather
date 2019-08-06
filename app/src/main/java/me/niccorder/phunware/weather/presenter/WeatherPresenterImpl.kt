package me.niccorder.phunware.weather.presenter

import io.reactivex.android.schedulers.AndroidSchedulers
import me.niccorder.phunware.data.repository.WeatherRepository
import me.niccorder.phunware.internal.ActivityScope
import me.niccorder.phunware.model.Location
import me.niccorder.phunware.weather.view.WeatherView
import javax.inject.Inject

/**
 * @see WeatherPresenter
 */
@ActivityScope
class WeatherPresenterImpl @Inject constructor(
  val view: WeatherView,
  private val weatherRepository: WeatherRepository
) : WeatherPresenter {

  private lateinit var location: Location

  override fun setLocation(location: Location) {
    this.location = location
  }

  override fun getLocation(): Location = location

  override fun onLoadWeather() {
    weatherRepository.getForecast(location)
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe({
        if (it.current?.temperature != null)
          view.setTemperature(it.current.temperature)
        if (it.current?.summary != null)
          view.setSummary(it.current.summary)
        if (it.current?.humidity != null) {
          view.setHumidity(it.current.humidity)
        }
      }, { view.showError(it) })
  }
}