package com.radiostudies.main.ui.di.module

import com.radiostudies.main.ui.MainActivity
import com.radiostudies.main.ui.di.UIBindingModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by eduardo.delito on 8/17/20.
 */
@Module
abstract class ActivityBindingModule {
    @ContributesAndroidInjector(modules = [
        UIBindingModule::class
    ])
    abstract fun bindMainActivity(): MainActivity
}
