package me.niccorder.phunware.weather

import dagger.Binds
import dagger.Module
import me.niccorder.scopes.ActivityScope
import me.niccorder.phunware.weather.presenter.WeatherPresenter
import me.niccorder.phunware.weather.presenter.WeatherPresenterImpl
import me.niccorder.phunware.weather.view.WeatherActivity
import me.niccorder.phunware.weather.view.WeatherView

@Module
abstract class WeatherModule {

    @Binds
    @me.niccorder.scopes.ActivityScope
    abstract fun weatherPresenter(
            weatherPresenterImpl: WeatherPresenterImpl
    ): WeatherPresenter

    @Binds
    @me.niccorder.scopes.ActivityScope
    abstract fun weatherView(weatherActivity: WeatherActivity): WeatherView
}