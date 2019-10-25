package me.niccorder.phunware

import android.content.Context
import me.niccorder.phunware.feature.weather.WeatherActivity
import me.niccorder.phunware.shared.Navigator
import me.niccorder.scopes.AppScope
import timber.log.Timber
import javax.inject.Inject

@AppScope
class PhunwareNavigator @Inject constructor() : Navigator {
  private val logger get() = Timber.tag("phunware-navigator")

  override fun toWeather(from: Context, zipCode: String) {
    logger.i("Navigating to weather for zipCode: $zipCode")
    from.startActivity(
      WeatherActivity.getIntent(from, zipCode)
    )
  }
}