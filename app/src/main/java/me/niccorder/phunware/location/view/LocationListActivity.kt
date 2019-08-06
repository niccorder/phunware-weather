package me.niccorder.phunware.location.view

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.jakewharton.rxbinding2.view.RxView
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_location_list.*
import me.niccorder.phunware.R
import me.niccorder.phunware.location.presenter.LocationListPresenter
import me.niccorder.phunware.model.Location
import me.niccorder.phunware.weather.view.WeatherActivity
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

/**
 * @see LocationListView
 */
class LocationListActivity : DaggerAppCompatActivity(), LocationListView {

  private val disposables: CompositeDisposable = CompositeDisposable()

  @Inject lateinit var presenter: LocationListPresenter
  @Inject lateinit var gson: Gson

  private lateinit var adapter: LocationAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_location_list)

    initRecycler()
    disposables.add(
      RxView.clicks(add_location_btn).subscribeBy(
        onNext = { presenter.onAddLocationClick() },
        onError = Timber::e
      )
    )

    presenter.onLoadLocations()
  }

  private fun initRecycler() {
    val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    location_recycler.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))
    location_recycler.layoutManager = layoutManager

    adapter = LocationAdapter(
      presenter::getLocation,
      presenter::locationCount,
      presenter::onLocationClicked
    )
    location_recycler.adapter = adapter

    adapter.notifyDataSetChanged()
  }

  override fun showAddLocationInput(show: Boolean) {
    // TODO(niccorder) – show location input
  }

  override fun notifyLocationAdded(position: Int) {
    adapter.notifyItemInserted(position)
  }

  override fun refreshLocations() {
    adapter.notifyDataSetChanged()
  }

  override fun showWeather(location: me.niccorder.phunware.model.Location) {
    startActivity(
      Intent(this, WeatherActivity::class.java).putExtra(
        me.niccorder.phunware.model.Location.KEY_LOCATION,
        gson.toJson(location)
      )
    )
  }

  override fun showError(throwable: Throwable) {
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

  override fun setAddLocationInputError(throwable: Throwable?) {
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