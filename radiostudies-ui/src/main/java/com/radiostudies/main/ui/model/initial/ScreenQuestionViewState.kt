package com.radiostudies.main.ui.model.initial

/**
 * Created by eduardo.delito on 9/18/20.
 */
sealed class ScreenQuestionViewState
class ScreenQuestionModel(val fileName: String?) : ScreenQuestionViewState()
class ScreenQuestionListModel(val screenQuestion: ScreenQuestion? = null, val selectedOption: List<String>) : ScreenQuestionViewState()
