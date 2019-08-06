package me.niccorder.phunware.data.local

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import me.niccorder.scopes.AppScope

@Module
class LocalModule {

    @me.niccorder.scopes.AppScope
    @Provides
    fun provideDatabase(context: Context): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "app-db"
    ).build()

    @me.niccorder.scopes.AppScope
    @Provides
    fun locationDao(appDatabase: AppDatabase): LocationDao = appDatabase.locationDao()
}