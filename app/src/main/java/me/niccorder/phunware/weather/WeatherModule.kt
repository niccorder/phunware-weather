package me.niccorder.phunware.weather

import dagger.Binds
import dagger.Module
import me.niccorder.phunware.weather.presenter.WeatherPresenter
import me.niccorder.phunware.weather.presenter.WeatherPresenterImpl
import me.niccorder.phunware.weather.view.WeatherActivity
import me.niccorder.phunware.weather.view.WeatherView
import me.niccorder.phunware.internal.WeatherScope

@Module
abstract class WeatherModule {

    @Binds
    @WeatherScope
    abstract fun weatherPresenter(
            weatherPresenterImpl: WeatherPresenterImpl
    ): WeatherPresenter

    @Binds
    @WeatherScope
    abstract fun weatherView(weatherActivity: WeatherActivity): WeatherView
}