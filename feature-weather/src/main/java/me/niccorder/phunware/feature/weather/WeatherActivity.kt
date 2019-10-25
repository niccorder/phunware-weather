package me.niccorder.phunware.feature.weather

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_weather.*
import me.niccorder.phunware.model.Forecast
import me.niccorder.phunware.model.Location
import me.niccorder.phunware.util.injection.lifecycle.AppViewModelFactory
import me.niccorder.util.rx.addTo
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class WeatherActivity : DaggerAppCompatActivity() {
  private val logger get() = Timber.tag("weather-activity")
  private val disposables = CompositeDisposable()

  @Inject
  lateinit var appViewModelFactory: AppViewModelFactory

  private val weatherViewModel: WeatherViewModel by viewModels { appViewModelFactory }

  @SuppressLint("SetTextI18n")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_weather)

    val zipCode = intent.getStringExtra(KEY_ZIP_CODE)
    weatherViewModel.setZipCode(zipCode)

    weatherViewModel.location.subscribeBy(
      onNext = ::setLocation,
      onError = { throwable ->
        logger.e(throwable, "Error while observing location changes")
      }
    ) addTo disposables

    weatherViewModel.forecast.subscribeBy(
      onNext = ::setForecast,
      onError = { throwable ->
        logger.e(throwable, "Error while observing forecast changes")
      }
    ) addTo disposables
  }

  private fun setLocation(location: Location) {
    zip_code.text = location.zipCode
    latitude.text = "${location.latitude}"
    longitude.text = "${location.longitude}"
  }

  private fun setForecast(forecast: Forecast) {
    forecast.current?.let {
      setSummary(it.summary)
      if (it.humidity != null) setHumidity(it.humidity!!)
      if (it.temperature != null) setTemperature(it.temperature!!)
    }
  }

  private fun setTemperature(temperature: Double) {
    this.temperature.text = temperature.toString()
  }

  private fun setHumidity(humidity: Double) {
    humidity_row.visibility = View.VISIBLE
    this.humidity.text = humidity.toString()
  }

  private fun setSummary(summary: String?) {
    summary_row.visibility = View.VISIBLE
    this.summary.text = summary
  }

  private fun showError(throwable: Throwable) {
    val errorMessage = when (throwable) {
      is IOException -> getString(R.string.network_error)
      is HttpException -> getString(R.string.network_error)
      else -> getString(R.string.unknown_error)
    }

    Snackbar.make(root, errorMessage, Snackbar.LENGTH_SHORT).show()
  }

  override fun onDestroy() {
    super.onDestroy()
    disposables.clear()
  }

  companion object {
    private const val KEY_ZIP_CODE = "key_zip_code"

    @JvmStatic
    fun getIntent(
      from: Context,
      zipCode: String
    ) = Intent(from, WeatherActivity::class.java).apply {
      putExtra(KEY_ZIP_CODE, zipCode)
    }
  }
}