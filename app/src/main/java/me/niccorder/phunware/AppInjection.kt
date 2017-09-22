package me.niccorder.phunware

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.location.Geocoder
import android.location.LocationManager
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import me.niccorder.phunware.data.DataModule
import me.niccorder.phunware.internal.AppScope
import me.niccorder.phunware.internal.LocationListScope
import me.niccorder.phunware.internal.WeatherScope
import me.niccorder.phunware.location.data.LocationRepositoryImpl
import me.niccorder.phunware.location.list.LocationListModule
import me.niccorder.phunware.location.list.view.LocationListActivity
import me.niccorder.phunware.weather.WeatherModule
import me.niccorder.phunware.weather.data.WeatherRepository
import me.niccorder.phunware.weather.data.WeatherRepositoryImpl
import me.niccorder.phunware.weather.view.WeatherActivity

@Module(includes = arrayOf(
        DataModule::class,
        PhunwareActivityInjectors::class
))
class AppModule {

    @Provides
    @AppScope
    fun appContext(app: Application): Context = app

    @Provides
    @AppScope
    fun activityManager(
            context: Context
    ): ActivityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

    @Provides
    fun memoryInfo(activityManager: ActivityManager): ActivityManager.MemoryInfo {
        val memInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memInfo)
        return memInfo
    }

    @Provides
    @AppScope
    fun locationManager(
            context: Context
    ): LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    @Provides
    @AppScope
    fun geocoder(context: Context): Geocoder = Geocoder(context)

    @Provides
    @AppScope
    fun locationRepository(
            locationRepositoryImpl: LocationRepositoryImpl
    ): me.niccorder.phunware.location.data.LocationRepository = locationRepositoryImpl

    @Provides
    @AppScope
    fun weatherRepository(
            weatherRepositoryImpl: WeatherRepositoryImpl
    ): WeatherRepository = weatherRepositoryImpl
}

@Module
abstract class PhunwareActivityInjectors {

    @LocationListScope
    @ContributesAndroidInjector(modules = arrayOf(LocationListModule::class))
    internal abstract fun homeActivity(): LocationListActivity

    @WeatherScope
    @ContributesAndroidInjector(modules = arrayOf(WeatherModule::class))
    internal abstract fun weatherActivity(): WeatherActivity
}

@AppScope
@Component(modules = arrayOf(
        AndroidSupportInjectionModule::class,
        AppModule::class
))
interface AppComponent : AndroidInjector<PhunwareApplication> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<PhunwareApplication>() {

        @BindsInstance
        abstract fun application(app: Application): Builder

        abstract fun dataModule(dataModule: DataModule): Builder
    }
}