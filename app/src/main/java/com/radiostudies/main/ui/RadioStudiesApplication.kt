package com.radiostudies.main.ui

import com.radiostudies.main.ui.di.component.DaggerRadioStudiesComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

/**
 * Created by eduardo.delito on 8/17/20.
 */
class RadioStudiesApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerRadioStudiesComponent
            .builder()
            .application(this)
            .build()
    }
}
