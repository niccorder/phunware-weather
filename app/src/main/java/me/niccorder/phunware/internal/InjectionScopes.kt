package me.niccorder.phunware.internal

import android.content.ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW
import dagger.android.ReleaseReferencesAt
import javax.inject.Scope

/**
 * A Dagger2 scope which is used for application wide objects. This is essentially a singleton.
 */
@Scope
@Retention(AnnotationRetention.RUNTIME)
@ReleaseReferencesAt(value = TRIM_MEMORY_RUNNING_LOW)
annotation class AppScope

/**
 * A Dagger2 scope for objects used in the home activity.
 */
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class LocationListScope

/**
 * A Dagger2 scope for objects used for displaying weather for a location.
 */
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class WeatherScope