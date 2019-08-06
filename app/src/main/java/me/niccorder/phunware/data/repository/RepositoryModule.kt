package me.niccorder.phunware.data.repository

import dagger.Binds
import dagger.Module
import me.niccorder.scopes.AppScope

@Module
abstract class RepositoryModule {

    @Binds
    @AppScope
    abstract fun locationRepository(impl: LocationRepositoryImpl): me.niccorder.phunware.data.LocationRepository

    @Binds
    @AppScope
    abstract fun weatherRepository(impl: WeatherRepositoryImpl): me.niccorder.phunware.data.WeatherRepository

}