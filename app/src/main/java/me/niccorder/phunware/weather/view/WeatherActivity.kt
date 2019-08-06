package me.niccorder.phunware.weather.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_weather.*
import me.niccorder.phunware.R
import me.niccorder.phunware.model.Location
import me.niccorder.phunware.weather.presenter.WeatherPresenter
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * @see WeatherView
 */
class WeatherActivity : DaggerAppCompatActivity(), WeatherView {

  @Inject lateinit var presenter: WeatherPresenter
  @Inject lateinit var gson: Gson

  @SuppressLint("SetTextI18n")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_weather)

    presenter.location = getLocation(savedInstanceState)

    zip_code.text = presenter.location.zipCode
    latitude.text = "${presenter.location.latitude}"
    longitude.text = "${presenter.location.longitude}"
    presenter.onLoadWeather()
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)

    outState.putString(
      Location.KEY_LOCATION,
      gson.toJson(presenter.location)
    )
  }

  private fun getLocation(savedInstanceState: Bundle?): Location = when {
    savedInstanceState != null -> gson.fromJson(
      savedInstanceState.getString(Location.KEY_LOCATION),
      Location::class.java
    )
    intent != null -> gson.fromJson(
      intent.getStringExtra(Location.KEY_LOCATION),
      Location::class.java
    )
    else -> throw IllegalStateException("You must provide a location.")
  }

  override fun setTemperature(temperature: Double) {
    this.temperature.text = temperature.toString()
  }

  override fun setHumidity(humidity: Double) {
    humidity_row.visibility = View.VISIBLE
    this.humidity.text = humidity.toString()
  }

  override fun setSummary(summary: String?) {
    summary_row.visibility = View.VISIBLE
    this.summary.text = summary
  }

  override fun showError(throwable: Throwable) {
    val errorMessage = when (throwable) {
      is IOException -> getString(R.string.network_error)
      is HttpException -> getString(R.string.network_error)
      else -> getString(R.string.unknown_error)
    }

    Snackbar.make(root, errorMessage, Snackbar.LENGTH_SHORT).show()
  }
}