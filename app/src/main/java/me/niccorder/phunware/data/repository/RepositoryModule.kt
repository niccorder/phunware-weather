package me.niccorder.phunware.data.repository

import dagger.Binds
import dagger.Module
import me.niccorder.scopes.AppScope

@Module
abstract class RepositoryModule {

    @Binds
    @me.niccorder.scopes.AppScope
    abstract fun locationRepository(impl: LocationRepositoryImpl): LocationRepository

    @Binds
    @me.niccorder.scopes.AppScope
    abstract fun weatherRepository(impl: WeatherRepositoryImpl): WeatherRepository

}