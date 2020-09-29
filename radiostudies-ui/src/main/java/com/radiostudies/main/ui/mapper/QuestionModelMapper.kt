package com.radiostudies.main.ui.mapper

import com.radiostudies.main.db.entity.QuestionEntity
import com.radiostudies.main.ui.model.Question

/**
 * Created by eduardo.delito on 9/29/20.
 */

fun List<Question>.questionModelToQuestionEntity() : List<QuestionEntity> {
    return this.map {
        QuestionEntity(
            id = 0,
            question = it.question,
            answers = it.answer
        )
    }
}
