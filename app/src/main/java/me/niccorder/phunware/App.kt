package me.niccorder.phunware

import dagger.android.support.DaggerApplication
import timber.log.Timber

/**
 * Our Application's entry point (or one of them at least)!
 */
class App : DaggerApplication() {

  override fun applicationInjector() = DaggerAppComponent.factory().create(this)

  override fun onCreate() {
    super.onCreate()

    if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
  }
}