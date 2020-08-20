package com.radiostudies.main.ui.di.module

import androidx.lifecycle.ViewModelProvider
import com.radiostudies.main.common.viewmodel.ViewModelFactory
import com.radiostudies.main.ui.di.UIViewModelModule
import dagger.Binds
import dagger.Module

/**
 * Created by eduardo.delito on 8/17/20.
 */
@Module(
    includes = [
        UIViewModelModule::class
    ]
)
abstract class ViewModelBindingModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
