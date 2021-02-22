package com.radiostudies.main.ui.di.module

import com.radiostudies.main.common.di.CommonModule
import com.radiostudies.main.di.ClientModule
import com.radiostudies.main.di.DBModule
import dagger.Module

/**
 * Created by eduardo.delito on 8/22/20.
 */
@Module(includes = [
    DBModule::class,
    ClientModule::class,
    CommonModule::class
])
class AppModule