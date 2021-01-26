package com.radiostudies.main.common.di

import android.content.Context
import android.content.SharedPreferences
import com.radiostudies.main.common.manager.SharedPreferencesManager
import com.radiostudies.main.common.manager.SharedPreferencesManagerImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CommonModule(private val context: Context) {
    @Provides
    @Singleton
    fun provideContext() = context

    @Provides
    @Singleton
    fun provideSharedPreferences(): SharedPreferences =
        context.getSharedPreferences(STORE_NAME, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideSharedPreferencesManager(sharedPreferences: SharedPreferences): SharedPreferencesManager =
        SharedPreferencesManagerImpl(sharedPreferences)

    companion object {
        private const val STORE_NAME = "movies_prefs"
    }
}
