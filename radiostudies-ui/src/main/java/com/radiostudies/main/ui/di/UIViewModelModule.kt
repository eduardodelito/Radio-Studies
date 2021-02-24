package com.radiostudies.main.ui.di

import androidx.lifecycle.ViewModel
import com.radiostudies.main.LoginRepository
import com.radiostudies.main.common.manager.SharedPreferencesManager
import com.radiostudies.main.common.viewmodel.ViewModelKey
import com.radiostudies.main.db.manager.ActualManager
import com.radiostudies.main.db.manager.DBManager
import com.radiostudies.main.db.manager.QuestionManager
import com.radiostudies.main.repository.DiariesRepository
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
    fun provideLoginViewModel(dbManager: DBManager, loginRepository: LoginRepository, sharedPreferencesManager: SharedPreferencesManager): ViewModel =
        LoginViewModel(dbManager, loginRepository, sharedPreferencesManager)

    @Provides
    @IntoMap
    @ViewModelKey(InitialQuestionsViewModel::class)
    fun provideInitialQuestionsViewModel(questionManager: QuestionManager): ViewModel =
        InitialQuestionsViewModel(questionManager)

    @Provides
    @IntoMap
    @ViewModelKey(MainInfoViewModel::class)
    fun provideMainInfoViewModel(
        actualManager: ActualManager,
        sharedPreferencesManager: SharedPreferencesManager
    ): ViewModel =
        MainInfoViewModel(actualManager, sharedPreferencesManager)

    @Provides
    @IntoMap
    @ViewModelKey(ActualQuestionsViewModel::class)
    fun provideActualQuestionsViewModel(
        actualManager: ActualManager, sharedPreferencesManager: SharedPreferencesManager
    ): ViewModel =
        ActualQuestionsViewModel(actualManager, sharedPreferencesManager)

    @Provides
    @IntoMap
    @ViewModelKey(DiaryViewModel::class)
    fun provideDiaryViewModel(
        actualManager: ActualManager,
        diariesRepository: DiariesRepository
    ): ViewModel =
        DiaryViewModel(actualManager, diariesRepository)

    @Provides
    @IntoMap
    @ViewModelKey(DiaryDetailsViewModel::class)
    fun provideDiaryDetailsViewModel(
        actualManager: ActualManager,
        diariesRepository: DiariesRepository
    ): ViewModel =
        DiaryDetailsViewModel(actualManager, diariesRepository)

    @Provides
    @IntoMap
    @ViewModelKey(AddDiaryViewModel::class)
    fun provideAddDiaryViewModel(actualManager: ActualManager): ViewModel =
        AddDiaryViewModel(actualManager)
}
