package me.niccorder.phunware.weather.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import com.google.gson.Gson
import dagger.android.support.DaggerAppCompatActivity
import kotterknife.bindView
import me.niccorder.phunware.R
import me.niccorder.phunware.data.model.Location
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

    private val root: View by bindView(R.id.root)
    private val temperature: TextView by bindView(R.id.temperature)
    private val zipText: TextView by bindView(R.id.zip_code)
    private val latitudeText: TextView by bindView(R.id.latitude)
    private val longitudeText: TextView by bindView(R.id.longitude)

    private val humidityRow: TableRow by bindView(R.id.humidity_row)
    private val humidity: TextView by bindView(R.id.humidity)

    private val summaryRow: TableRow by bindView(R.id.summary_row)
    private val summary: TextView by bindView(R.id.summary)

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        presenter.location = getLocation(savedInstanceState)

        zipText.text = presenter.location.zipCode
        latitudeText.text = "${presenter.location.latitude}"
        longitudeText.text = "${presenter.location.longitude}"
        presenter.onLoadWeather()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putString(
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
        humidityRow.visibility = View.VISIBLE
        this.humidity.text = humidity.toString()
    }

    override fun setSummary(summary: String?) {
        summaryRow.visibility = View.VISIBLE
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