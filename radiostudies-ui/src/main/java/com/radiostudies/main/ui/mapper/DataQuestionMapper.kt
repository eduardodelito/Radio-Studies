package com.radiostudies.main.ui.mapper

import com.radiostudies.main.db.entity.DataQuestionEntity
import com.radiostudies.main.db.model.DataQuestion

/**
 * Created by eduardo.delito on 10/11/20.
 */

fun List<DataQuestionEntity>.dataQuestionsEntityToDataQuestions(): List<DataQuestion> {
    return this.map {
        DataQuestion(
            code = it.code,
            header = it.header,
            question = it.question,
            options = it.options
        )
    }
}