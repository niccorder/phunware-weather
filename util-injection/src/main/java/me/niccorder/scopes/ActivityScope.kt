package me.niccorder.scopes

import javax.inject.Scope


/**
 * Scope of dependencies that should live the entirety of the activity. Objects which are dependent
 * on the context, should always live this scope, or below it in the dependency graph.
 */
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ActivityScope