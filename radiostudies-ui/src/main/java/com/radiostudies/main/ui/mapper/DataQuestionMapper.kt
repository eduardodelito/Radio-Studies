package com.radiostudies.main.ui.mapper

import com.radiostudies.main.db.entity.DataQuestionEntity
import com.radiostudies.main.model.DataQuestion

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

fun DataQuestion.dataQuestionModelToDataQuestionEntity(): DataQuestionEntity {
    return DataQuestionEntity(
        id = 0,
        code = code,
        header = header,
        question = question,
        options = options
    )
}

fun DataQuestionEntity.dataQuestionEntityToDataQuestionModel(): DataQuestion {
    return DataQuestion(
        code = code,
        header = header,
        question = question,
        options = options
    )
}
