package com.radiostudies.main.ui.di.module

import com.radiostudies.main.di.DBModule
import dagger.Module

/**
 * Created by eduardo.delito on 8/22/20.
 */
@Module(includes = [
    DBModule::class
])
class AppModule