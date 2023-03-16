package com.example.transweather.core.storage_manager.di

import android.content.Context
import com.example.transweather.core.storage_manager.SharedPreferencesManager
import com.example.transweather.core.storage_manager.StorageManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageManagerModule {

    @Singleton
    @Provides
    fun provideStorageManager(@ApplicationContext context: Context): StorageManager =
        SharedPreferencesManager(context)

}