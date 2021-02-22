package com.radiostudies.main.di

import com.radiostudies.main.LoginRepository
import com.radiostudies.main.LoginRepositoryImpl
import com.radiostudies.main.client.RadioStudiesApiClient
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
class ClientModule {
    @Provides
    @Singleton
    fun provideHttpClient() = OkHttpClient()

    @Provides
    @Singleton
    fun provideHttpClientBuilder() = OkHttpClient.Builder()

    @Provides
    @Singleton
    fun provideRadioStudiesApiClient(okHttpClient: OkHttpClient.Builder) =
        RadioStudiesApiClient(okHttpClient)

    @Provides
    @Singleton
    fun provideLoginRepository(client: RadioStudiesApiClient): LoginRepository =
        LoginRepositoryImpl(client)
}
