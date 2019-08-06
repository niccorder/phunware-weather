package me.niccorder.phunware.weather.presenter

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import me.niccorder.phunware.model.Location
import me.niccorder.phunware.weather.view.WeatherView
import me.niccorder.scopes.ActivityScope
import javax.inject.Inject

/**
 * @see WeatherPresenter
 */
@ActivityScope
class WeatherPresenterImpl @Inject constructor(
  val view: WeatherView,
  private val weatherRepository: me.niccorder.phunware.data.WeatherRepository
) : WeatherPresenter {

  private lateinit var location: Location

  override fun setLocation(location: Location) {
    this.location = location
  }

  override fun getLocation(): Location = location

  override fun onLoadWeather() {
    weatherRepository.getForecast(location)
      .observeOn(AndroidSchedulers.mainThread())
      .subscribeBy(
        onNext = { forecast ->
        forecast.current?.let { data ->
          data.temperature?.let(view::setTemperature)
          data.summary?.let(view::setSummary)
          data.humidity?.let(view::setHumidity)
        }
      },
        onError = { throwable -> view.showError(throwable) }
      )
  }
}