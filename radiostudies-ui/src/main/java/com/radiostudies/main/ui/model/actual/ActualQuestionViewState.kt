package com.radiostudies.main.ui.model.actual

import com.radiostudies.main.db.entity.Option
import com.radiostudies.main.db.model.ActualQuestion

/**
 * Created by eduardo.delito on 10/4/20.
 */
sealed class ActualQuestionViewState
data class AreaForm(val fileName: String?, val genderCode: String?) :
    ActualQuestionViewState()

data class ActualQuestionForm(val fileName: String?, val genderCode: String?) :
    ActualQuestionViewState()

data class ActualQuestionModel(
    val actualQuestion: ActualQuestion,
    val isPrevEnable: Boolean,
    val isNextEnable: Boolean,
    val isPlus: Boolean
) :
    ActualQuestionViewState()

data class SelectedOptionForm(val selectedOptions: MutableList<Option>) :
    ActualQuestionViewState()

data class ActualQuestionComplete(val isComplete: Boolean) : ActualQuestionViewState()
