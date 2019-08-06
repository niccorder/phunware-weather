package me.niccorder.phunware.location

import dagger.Binds
import dagger.Module
import me.niccorder.scopes.ActivityScope
import me.niccorder.phunware.location.presenter.LocationListPresenter
import me.niccorder.phunware.location.presenter.LocationListPresenterImpl
import me.niccorder.phunware.location.view.LocationListActivity
import me.niccorder.phunware.location.view.LocationListView

@Module
abstract class LocationModule {

    @Binds
    @me.niccorder.scopes.ActivityScope
    abstract fun view(impl: LocationListActivity): LocationListView

    @Binds
    @me.niccorder.scopes.ActivityScope
    abstract fun presenter(impl: LocationListPresenterImpl): LocationListPresenter
}