package com.radiostudies.main.ui.model

/**
 * Created by eduardo.delito on 9/18/20.
 */
sealed class ScreenQuestionState
class ScreenQuestionModel(val fileName: String?) : ScreenQuestionState()
class ScreenQuestionListModel(val screenQuestion: ScreenQuestion? = null) : ScreenQuestionState()
class SwitchScreenModel(val isSwitchScreen: Boolean) : ScreenQuestionState()
