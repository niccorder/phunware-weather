package me.niccorder.phunware

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.location.Geocoder
import android.location.LocationManager
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import me.niccorder.phunware.data.local.LocalModule
import me.niccorder.phunware.data.remote.RemoteModule
import me.niccorder.phunware.data.repository.RepositoryModule
import me.niccorder.phunware.location.LocationModule
import me.niccorder.phunware.location.view.LocationListActivity
import me.niccorder.phunware.weather.WeatherModule
import me.niccorder.phunware.weather.view.WeatherActivity
import me.niccorder.scopes.ActivityScope
import me.niccorder.scopes.AppScope

@Module
abstract class AppBindingModule {
    @Binds
    @AppScope
    abstract fun appContext(app: Application): Context
}

@Module(includes = [AppBindingModule::class])
class AppModule {

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
}

@Module
abstract class GeneratedActivityInjectors {

    @ActivityScope
    @ContributesAndroidInjector(modules = [LocationModule::class])
    internal abstract fun homeActivity(): LocationListActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [WeatherModule::class])
    internal abstract fun weatherActivity(): WeatherActivity
}

@AppScope
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        GeneratedActivityInjectors::class,
        AppModule::class,
        RemoteModule::class,
        LocalModule::class,
        RepositoryModule::class
    ]
)
interface AppComponent : AndroidInjector<PhunwareApplication> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application): AppComponent
    }
}