package me.niccorder.phunware.location.presenter

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import me.niccorder.phunware.data.repository.LocationRepository
import me.niccorder.phunware.internal.ActivityScope
import me.niccorder.phunware.location.view.LocationListView
import me.niccorder.phunware.model.Location
import timber.log.Timber
import javax.inject.Inject

/**
 * @see LocationListPresenter
 */
@ActivityScope
class LocationListPresenterImpl @Inject constructor(
  internal val view: LocationListView,
  private val locationRepository: LocationRepository
) : LocationListPresenter {

  private val disposables: CompositeDisposable = CompositeDisposable()
  private val locations: MutableList<me.niccorder.phunware.model.Location> = mutableListOf()

  override fun destroy() {
    disposables.clear()
  }

  override fun getLocation(position: Int): me.niccorder.phunware.model.Location = locations[position]

  override fun locationCount(): Int = locations.size

  override fun onLocationClicked(position: Int) {
    view.showWeather(locations[position])
  }

  override fun onLoadLocations() {
    disposables.add(
      locationRepository.locations
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::onLoadLocationsSuccess, Timber::e)
    )
  }

  internal fun onLoadLocationsSuccess(locations: MutableList<me.niccorder.phunware.model.Location>) {
    Timber.d("onLoadLocationsSuccess()")

    if (this.locations.isEmpty()) {
      this.locations.addAll(locations)
    } else {
      locations.forEach {
        if (!this.locations.contains(it)) this.locations.add(it)
      }
    }

    view.refreshLocations()
  }

  override fun onAddLocationClick() {
    Timber.d("onAddLocationClick()")

    view.showAddLocationInput(true)
  }

  override fun onLocationAdded(location: me.niccorder.phunware.model.Location) {
    Timber.d("onLocationAdded()")

    locations.add(location)
    view.notifyLocationAdded(locations.size - 1)
  }

  override fun onAddLocation(zipCode: String?) {
    if (zipCode == null || zipCode.length != 5) {
      view.setAddLocationInputError(IllegalArgumentException())
      return
    }

    try {
      zipCode.toInt()
    } catch (e: NumberFormatException) {
      view.setAddLocationInputError(e)
      return
    }

    disposables.add(
      locationRepository.getLocation(zipCode)
        .flatMapSingle { locationRepository.addLocation(it) }
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
          { if (!locations.contains(it)) onLocationAdded(it) },
          { view.showError(it) }
        )
    )
  }
}