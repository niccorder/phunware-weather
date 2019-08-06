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
import me.niccorder.phunware.data.DataModule
import me.niccorder.scopes.ActivityScope
import me.niccorder.scopes.AppScope
import me.niccorder.phunware.location.LocationModule
import me.niccorder.phunware.location.view.LocationListActivity
import me.niccorder.phunware.weather.WeatherModule
import me.niccorder.phunware.weather.view.WeatherActivity

@Module
abstract class AppBindingModule {
    @Binds
    @me.niccorder.scopes.AppScope
    abstract fun appContext(app: Application): Context
}

@Module(includes = [AppBindingModule::class])
class AppModule {

    @Provides
    @me.niccorder.scopes.AppScope
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
    @me.niccorder.scopes.AppScope
    fun locationManager(
        context: Context
    ): LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    @Provides
    @me.niccorder.scopes.AppScope
    fun geocoder(context: Context): Geocoder = Geocoder(context)
}

@Module
abstract class PhunwareActivityInjectors {

    @me.niccorder.scopes.ActivityScope
    @ContributesAndroidInjector(modules = [LocationModule::class])
    internal abstract fun homeActivity(): LocationListActivity

    @me.niccorder.scopes.ActivityScope
    @ContributesAndroidInjector(modules = [WeatherModule::class])
    internal abstract fun weatherActivity(): WeatherActivity
}

@me.niccorder.scopes.AppScope
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        DataModule::class,
        PhunwareActivityInjectors::class
    ]
)
interface AppComponent : AndroidInjector<PhunwareApplication> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application): AppComponent
    }
}