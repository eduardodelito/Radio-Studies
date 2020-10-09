package com.radiostudies.main.ui.mapper

import com.radiostudies.main.db.entity.QuestionEntity
import com.radiostudies.main.ui.model.initial.QuestionInitial

/**
 * Created by eduardo.delito on 9/29/20.
 */

fun List<QuestionInitial>.questionModelToQuestionEntity() : List<QuestionEntity> {
    return this.map {
        QuestionEntity(
            id = 0,
            question = it.question,
            answers = it.answer
        )
    }
}
