package me.niccorder.phunware.data.remote.http

import android.annotation.SuppressLint
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import java.io.IOException


/**
 * A kotlin singleton object which intercepts all okhttp3 requests and logs them to [Timber].
 */
object LoggingInterceptor : Interceptor {

    @SuppressLint("DefaultLocale")
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        Timber.tag("Network").v(
                String.format(
                        "%s\t%s\t\t%d",
                        request.method(),
                        request.url(),
                        response.code()
                )
        )
        return response
    }
}