package com.radiostudies.main.ui.di.component

import android.app.Application
import com.radiostudies.main.di.DBModule
import com.radiostudies.main.ui.RadioStudiesApplication
import com.radiostudies.main.ui.di.module.ActivityBindingModule
import com.radiostudies.main.ui.di.module.AppModule
import com.radiostudies.main.ui.di.module.ViewModelBindingModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

/**
 * Created by eduardo.delito on 8/17/20.
 */
@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        ActivityBindingModule::class,
        ViewModelBindingModule::class,
        AppModule::class
    ]
)
interface RadioStudiesComponent : AndroidInjector<RadioStudiesApplication> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun database(dbModule: DBModule): Builder
        fun build(): RadioStudiesComponent
    }
}

