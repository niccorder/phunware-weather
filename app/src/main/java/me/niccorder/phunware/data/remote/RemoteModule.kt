package me.niccorder.phunware.data.remote

import android.content.Context
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import me.niccorder.phunware.BuildConfig
import me.niccorder.phunware.data.remote.api.LocationApi
import me.niccorder.phunware.data.remote.api.WeatherApi
import me.niccorder.phunware.data.remote.http.CacheAllInterceptor
import me.niccorder.phunware.data.remote.http.LoggingInterceptor
import me.niccorder.scopes.AppScope
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class RemoteModule {

    @me.niccorder.scopes.AppScope
    @Provides
    fun provideGson(): Gson = Gson()

    @me.niccorder.scopes.AppScope
    @Provides
    fun provideNetworkCache(context: Context): Cache = Cache(
        context.cacheDir,
        10 * 1024 * 1024
    )

    @me.niccorder.scopes.AppScope
    @Provides
    fun provideCallAdapterFactory(): CallAdapter.Factory =
        RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())

    @me.niccorder.scopes.AppScope
    @Provides
    fun provideCallConverterFactory(
        gson: Gson
    ): Converter.Factory = GsonConverterFactory.create(gson)

    @me.niccorder.scopes.AppScope
    @Provides
    fun providesOkhttpClientBuilder(): OkHttpClient.Builder = OkHttpClient.Builder().apply {
        addInterceptor(LoggingInterceptor)
        retryOnConnectionFailure(true)
    }

    @me.niccorder.scopes.AppScope
    @Provides
    fun provideRetrofitBuilder(
        converterFactory: Converter.Factory,
        adapterFactory: CallAdapter.Factory
    ): Retrofit.Builder = Retrofit.Builder().apply {
        addConverterFactory(converterFactory)
        addCallAdapterFactory(adapterFactory)
        validateEagerly(BuildConfig.DEBUG)
    }

    @me.niccorder.scopes.AppScope
    @Provides
    fun locationApi(
        okhttpBuilder: OkHttpClient.Builder,
        retrofitBuilder: Retrofit.Builder
    ): LocationApi = retrofitBuilder.apply {
        baseUrl(BuildConfig.LOCATION_BASE_URL)
        client(
            okhttpBuilder.addNetworkInterceptor(CacheAllInterceptor).build()
        )
    }.build().create(LocationApi::class.java)

    @me.niccorder.scopes.AppScope
    @Provides
    fun weatherApi(
        okhttpBuilder: OkHttpClient.Builder,
        retrofitBuilder: Retrofit.Builder
    ): WeatherApi = retrofitBuilder.apply {
        baseUrl(BuildConfig.WEATHER_BASE_URL)
        client(okhttpBuilder.build())
    }.build().create(WeatherApi::class.java)
}