package com.radiostudies.main.ui.model.actual

import com.radiostudies.main.db.model.ActualQuestion

/**
 * Created by eduardo.delito on 10/4/20.
 */
sealed class ActualQuestionState
data class AreaForm(val fileName: String?) :
    ActualQuestionState()

data class ActualQuestionForm(val fileName: String?) :
    ActualQuestionState()

data class ActualQuestionModel(val actualQuestion: ActualQuestion?, val isPrevEnable: Boolean, val isNextEnable: Boolean) :
    ActualQuestionState()