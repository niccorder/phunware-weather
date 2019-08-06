package me.niccorder.phunware.internal

import javax.inject.Scope

/**
 * A Dagger2 scope which is used for application wide objects. This is essentially a singleton.
 */
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class AppScope

/**
 * Scope of dependencies that should live the entirety of the activity. Objects which are dependent
 * on the context, should always live this scope, or below it in the dependency graph.
 */
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ActivityScope