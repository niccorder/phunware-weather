package me.niccorder.phunware.location.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.clicks
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_location_list.*
import me.niccorder.phunware.R
import me.niccorder.phunware.feature.weather.WeatherActivity
import me.niccorder.phunware.location.LocationListViewModel
import me.niccorder.phunware.model.Location
import me.niccorder.phunware.util.injection.lifecycle.AppViewModelFactory
import me.niccorder.util.rx.addTo
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class LocationListActivity : DaggerAppCompatActivity() {
  private val logger get() = Timber.tag("location-list-activity")

  private val disposables: CompositeDisposable = CompositeDisposable()

  @Inject lateinit var appVmFactory: AppViewModelFactory
  @Inject lateinit var locationAdapter: LocationAdapter

  private val locationListViewModel: LocationListViewModel by viewModels { appVmFactory }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_location_list)

    location_recycler.apply {
      adapter = locationAdapter
      addItemDecoration(
        DividerItemDecoration(
          this@LocationListActivity,
          (layoutManager as LinearLayoutManager).orientation
        )
      )
    }

    locationAdapter.locationClicks.subscribeBy { showWeather(it) } addTo disposables

    locationListViewModel.locations.subscribeBy(
      onNext = { locations -> locationAdapter.submitList(locations) },
      onError = { throwable -> logger.e(throwable, "Error observing locations") }
    ) addTo disposables

    add_location_btn.clicks().subscribeBy(
      onNext = { showLocationInput() },
      onError = logger::e
    ) addTo disposables
  }

  private fun showLocationInput() {

  }

  private fun showWeather(location: Location) {
    startActivity(
      Intent(this, WeatherActivity::class.java).apply {
        putExtra(Location.KEY_LOCATION, location.zipCode)
      }
    )
  }

  fun showError(throwable: Throwable) {
    val errorMessage = when (throwable) {
      is IOException -> getString(R.string.network_error)
      is HttpException -> {
        if (throwable.code() != 404)
          getString(R.string.network_error)
        else getString(R.string.zip_not_found_error)
      }
      else -> getString(R.string.unknown_error)
    }

    Snackbar.make(root, errorMessage, Snackbar.LENGTH_SHORT).show()
  }

  fun setAddLocationInputError(throwable: Throwable?) {
//    addLocationDialog?.inputEditText?.error = when (throwable) {
//      null -> null
//      is IOException -> getString(R.string.network_error)
//      is HttpException -> getString(R.string.network_error)
//      is NumberFormatException -> getString(R.string.illegal_zip_error)
//      is IllegalArgumentException -> getString(R.string.zip_length_error)
//      else -> getString(R.string.unknown_error)
//    }
  }
}