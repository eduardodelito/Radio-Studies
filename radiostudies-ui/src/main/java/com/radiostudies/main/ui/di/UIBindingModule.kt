package com.radiostudies.main.ui.di

import androidx.lifecycle.ViewModelProvider
import com.radiostudies.main.ui.fragment.LoginFragment
import com.radiostudies.main.ui.fragment.MainFragment
import com.radiostudies.main.ui.viewmodel.LoginViewModel
import com.radiostudies.main.ui.viewmodel.MainViewModel
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

    @ContributesAndroidInjector(modules = [InjectMainViewModelModule::class])
    abstract fun bindMainFragment(): MainFragment

    @Module
    class InjectMainViewModelModule {
        @Provides
        internal fun provideMainViewModel(
            factory: ViewModelProvider.Factory,
            target: MainFragment
        ) = ViewModelProvider(target, factory).get(MainViewModel::class.java)
    }
}
