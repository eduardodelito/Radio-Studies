package com.radiostudies.main.ui.di

import androidx.lifecycle.ViewModel
import com.radiostudies.main.common.viewmodel.ViewModelKey
import com.radiostudies.main.db.manager.DBManager
import com.radiostudies.main.ui.viewmodel.LoginViewModel
import com.radiostudies.main.ui.viewmodel.MainViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

/**
 * Created by eduardo.delito on 8/17/20.
 */
@Module
class UIViewModelModule {
    @Provides
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    fun provideLoginViewModel(dbManager: DBManager): ViewModel =
        LoginViewModel(dbManager)

    @Provides
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun provideMainViewModel(): ViewModel =
        MainViewModel()
}
