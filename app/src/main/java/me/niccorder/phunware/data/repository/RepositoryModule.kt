package me.niccorder.phunware.data.repository

import dagger.Binds
import dagger.Module
import dagger.Provides
import me.niccorder.phunware.internal.AppScope

@Module
abstract class RepositoryModule {

    @Binds
    @AppScope
    abstract fun locationRepository(impl: LocationRepositoryImpl): LocationRepository

    @Binds
    @AppScope
    abstract fun weatherRepository(impl: WeatherRepositoryImpl): WeatherRepository

}