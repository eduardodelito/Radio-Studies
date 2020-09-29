package com.radiostudies.main.ui.di

import androidx.lifecycle.ViewModel
import com.radiostudies.main.common.viewmodel.ViewModelKey
import com.radiostudies.main.db.manager.DBManager
import com.radiostudies.main.db.manager.QuestionManager
import com.radiostudies.main.ui.viewmodel.*
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
    @ViewModelKey(InitialQuestionsViewModel::class)
    fun provideInitialQuestionsViewModel(questionManager: QuestionManager): ViewModel =
        InitialQuestionsViewModel(questionManager)

    @Provides
    @IntoMap
    @ViewModelKey(MainInfoViewModel::class)
    fun provideMainInfoViewModel(): ViewModel =
        MainInfoViewModel()

    @Provides
    @IntoMap
    @ViewModelKey(ActualQuestionsViewModel::class)
    fun provideActualQuestionsViewModel(): ViewModel =
        ActualQuestionsViewModel()

    @Provides
    @IntoMap
    @ViewModelKey(ActualQuestionsPage2ViewModel::class)
    fun provideActualQuestionsPage2ViewModel(): ViewModel =
        ActualQuestionsPage2ViewModel()
}
