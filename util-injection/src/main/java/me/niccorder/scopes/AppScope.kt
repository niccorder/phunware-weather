package me.niccorder.scopes

import javax.inject.Scope

/**
 * A Dagger2 scope which is used for application wide objects. This is essentially a singleton.
 */
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class AppScope