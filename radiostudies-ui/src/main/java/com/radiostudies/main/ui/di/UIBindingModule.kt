package com.radiostudies.main.ui.di

import androidx.lifecycle.ViewModelProvider
import com.radiostudies.main.ui.fragment.*
import com.radiostudies.main.ui.viewmodel.*
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

/**
 * Created by eduardo.delito on 8/17/20.
 */
@Module
abstract class UIBindingModule {
    @ContributesAndroidInjector(modules = [InjectLoginViewModelModule::class])
    abstract fun bindLoginFragment(): LoginFragment

    @Module
    class InjectLoginViewModelModule {
        @Provides
        internal fun provideLoginViewModel(
            factory: ViewModelProvider.Factory,
            target: LoginFragment
        ) = ViewModelProvider(target, factory).get(LoginViewModel::class.java)
    }

    @ContributesAndroidInjector(modules = [InjectInitialQuestionsViewModelModule::class])
    abstract fun bindInitialQuestionsFragment(): InitialQuestionsFragment

    @Module
    class InjectInitialQuestionsViewModelModule {
        @Provides
        internal fun provideInitialQuestionsViewModel(
            factory: ViewModelProvider.Factory,
            target: InitialQuestionsFragment
        ) = ViewModelProvider(target, factory).get(InitialQuestionsViewModel::class.java)
    }

    @ContributesAndroidInjector(modules = [InjectMainInfoViewModelModule::class])
    abstract fun bindMainInfoFragment(): MainInfoFragment

    @Module
    class InjectMainInfoViewModelModule {
        @Provides
        internal fun provideMainInfoViewModel(
            factory: ViewModelProvider.Factory,
            target: MainInfoFragment
        ) = ViewModelProvider(target, factory).get(MainInfoViewModel::class.java)
    }

    @ContributesAndroidInjector(modules = [InjectActualQuestionsViewModelModule::class])
    abstract fun bindActualQuestionsFragment(): ActualQuestionsFragment

    @Module
    class InjectActualQuestionsViewModelModule {
        @Provides
        internal fun provideActualQuestionsViewModel(
            factory: ViewModelProvider.Factory,
            target: ActualQuestionsFragment
        ) = ViewModelProvider(target, factory).get(ActualQuestionsViewModel::class.java)
    }

    @ContributesAndroidInjector(modules = [InjectActualQuestionsPage2ViewModelModule::class])
    abstract fun bindActualQuestionsPage2Fragment(): ActualQuestionsPage2Fragment

    @Module
    class InjectActualQuestionsPage2ViewModelModule {
        @Provides
        internal fun provideActualQuestionsPage2ViewModel(
            factory: ViewModelProvider.Factory,
            target: ActualQuestionsPage2Fragment
        ) = ViewModelProvider(target, factory).get(ActualQuestionsPage2ViewModel::class.java)
    }
}
