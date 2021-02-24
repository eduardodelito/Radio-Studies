package com.radiostudies.main.di

import com.radiostudies.main.LoginRepository
import com.radiostudies.main.LoginRepositoryImpl
import com.radiostudies.main.client.RadioStudiesApiClient
import com.radiostudies.main.repository.DiariesRepository
import com.radiostudies.main.repository.DiariesRepositoryImpl
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

    @Provides
    @Singleton
    fun provideDiariesRepository(client: RadioStudiesApiClient): DiariesRepository =
        DiariesRepositoryImpl(client)
}
