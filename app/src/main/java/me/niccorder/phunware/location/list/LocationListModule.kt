package me.niccorder.phunware.location.list

import dagger.Binds
import dagger.Module
import me.niccorder.phunware.location.list.presenter.LocationListPresenter
import me.niccorder.phunware.location.list.presenter.LocationListPresenterImpl
import me.niccorder.phunware.location.list.view.LocationListActivity
import me.niccorder.phunware.location.list.view.LocationListView
import me.niccorder.phunware.internal.LocationListScope


@Module
abstract class LocationListModule {

    @Binds
    @LocationListScope
    abstract fun view(locationListActivity: LocationListActivity): LocationListView

    @Binds
    @LocationListScope
    abstract fun presenter(
            locationListPresenterImpl: LocationListPresenterImpl
    ): LocationListPresenter
}