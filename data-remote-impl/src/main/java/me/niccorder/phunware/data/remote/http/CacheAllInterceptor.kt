package me.niccorder.phunware.data.remote.http

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

/**
 * A Kotlin singleton object which intercepts each network call and allows us to attach a cache
 * control header to help reduce the amount of times we ping an endpoint.
 */
object CacheAllInterceptor : Interceptor {

  private const val CACHE_CONTROL_KEY = "Cache-Control"
  private val cacheValue = CacheControl.Builder()
    .maxAge(2, TimeUnit.DAYS)
    .build()
    .toString()

  override fun intercept(chain: Interceptor.Chain?): Response {
    return chain?.proceed(chain.request())!!
      .newBuilder()
      .header(
        CACHE_CONTROL_KEY,
        cacheValue
      )
      .build()
  }
}