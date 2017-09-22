package me.niccorder.phunware

import dagger.android.AndroidInjector
import dagger.android.AndroidMemorySensitiveReferenceManager
import dagger.android.support.DaggerApplication
import me.niccorder.phunware.data.DataModule
import timber.log.Timber
import javax.inject.Inject

/**
 * Our Application's entry point (or one of them at least)!
 */
class PhunwareApplication : DaggerApplication() {

    private val appComponent: AndroidInjector<out DaggerApplication> = DaggerAppComponent.builder()
            .application(this)
            .dataModule(DataModule(this))
            .create(this)

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> = appComponent

    // TODO - Didn't have enough time to manage this.
    @Inject lateinit var memoryManager: AndroidMemorySensitiveReferenceManager

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)

        memoryManager.onTrimMemory(level)
    }
}