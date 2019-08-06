package me.niccorder.phunware.data

import dagger.Binds
import dagger.Module
import me.niccorder.scopes.AppScope

@Module
abstract class RepositoryModule {

    @Binds
    @AppScope
    abstract fun locationRepository(impl: LocationRepositoryImpl): LocationRepository

    @Binds
    @AppScope
    abstract fun weatherRepository(impl: WeatherRepositoryImpl): WeatherRepository

}