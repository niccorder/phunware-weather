package me.niccorder.phunware

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.location.Geocoder
import android.location.LocationManager
import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.multibindings.IntoMap
import me.niccorder.phunware.data.RepositoryModule
import me.niccorder.phunware.data.local.LocalModule
import me.niccorder.phunware.data.remote.RemoteModule
import me.niccorder.phunware.internal.ViewModelKey
import me.niccorder.phunware.location.LocationListViewModel
import me.niccorder.phunware.location.ui.LocationListActivity
import me.niccorder.phunware.weather.WeatherActivity
import me.niccorder.phunware.weather.WeatherViewModel
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
    @ContributesAndroidInjector
    internal abstract fun homeActivity(): LocationListActivity

    @ActivityScope
    @ContributesAndroidInjector
    internal abstract fun weatherActivity(): WeatherActivity
}

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(LocationListViewModel::class)
    abstract fun locationList(vm: LocationListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WeatherViewModel::class)
    abstract fun weatherVieWModel(vm: WeatherViewModel): ViewModel
}

@AppScope
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        GeneratedActivityInjectors::class,
        AppModule::class,
        ViewModelModule::class,
        RemoteModule::class,
        LocalModule::class,
        RepositoryModule::class
    ]
)
interface AppComponent : AndroidInjector<MainApp> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application): AppComponent
    }
}