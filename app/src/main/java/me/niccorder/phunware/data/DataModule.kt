package me.niccorder.phunware.data

import android.arch.persistence.room.Room
import android.content.Context
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import me.niccorder.phunware.BuildConfig
import me.niccorder.phunware.data.local.AppDatabase
import me.niccorder.phunware.data.local.LocationDao
import me.niccorder.phunware.data.remote.api.LocationApi
import me.niccorder.phunware.data.remote.api.WeatherApi
import me.niccorder.phunware.data.remote.http.CacheAllInterceptor
import me.niccorder.phunware.data.remote.http.LoggingInterceptor
import me.niccorder.phunware.internal.AppScope
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


@Module
class DataModule(val context: Context) {

    @AppScope
    @Provides
    fun provideGson(): Gson = Gson()

    @AppScope
    @Provides
    fun provideDatabase(): AppDatabase = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app-db"
    ).build()

    @AppScope
    @Provides
    fun locationDao(appDatabase: AppDatabase): LocationDao = appDatabase.locationDao()

    @AppScope
    @Provides
    fun provideNetworkCache(): Cache = Cache(context.cacheDir, 10 * 1024 * 1024)

    @AppScope
    @Provides
    fun provideCallAdapterFactory(): CallAdapter.Factory
            = RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())

    @AppScope
    @Provides
    fun provideCallConverterFactory(
            gson: Gson
    ): Converter.Factory = GsonConverterFactory.create(gson)

    @AppScope
    @Provides
    fun providesOkhttpClientBuilder(
            gson: Gson,
            cache: Cache
    ): OkHttpClient.Builder = OkHttpClient.Builder()
            .addInterceptor(LoggingInterceptor)
            .retryOnConnectionFailure(true)

    @AppScope
    @Provides
    fun provideRetrofitBuilder(
            converterFactory: Converter.Factory,
            adapterFactory: CallAdapter.Factory
    ): Retrofit.Builder = Retrofit.Builder()
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(adapterFactory)
            .validateEagerly(true)

    @AppScope
    @Provides
    fun locationApi(
            okhttpBuilder: OkHttpClient.Builder,
            retrofitBuilder: Retrofit.Builder
    ): LocationApi = retrofitBuilder
            .baseUrl(BuildConfig.LOCATION_BASE_URL)
            .client(
                    okhttpBuilder.addNetworkInterceptor(CacheAllInterceptor).build()
            )
            .build()
            .create(LocationApi::class.java)

    @AppScope
    @Provides
    fun weatherApi(
            okhttpBuilder: OkHttpClient.Builder,
            retrofitBuilder: Retrofit.Builder
    ): WeatherApi = retrofitBuilder
            .baseUrl(BuildConfig.WEATHER_BASE_URL)
            .client(okhttpBuilder.build())
            .build()
            .create(WeatherApi::class.java)
}