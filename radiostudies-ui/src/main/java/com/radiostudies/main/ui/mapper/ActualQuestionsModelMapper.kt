package com.radiostudies.main.ui.mapper

import com.radiostudies.main.db.entity.ActualQuestionEntity
import com.radiostudies.main.db.model.ActualQuestion

/**
 * Created by eduardo.delito on 10/5/20.
 */

fun List<ActualQuestion>.actualQuestionListModelToActualQuestionEntity(): List<ActualQuestionEntity> {
    return this.map {
        ActualQuestionEntity(
            id = 0,
            qId = it.qId,
            code = it.code,
            header = it.header,
            question = it.question,
            type = it.type,
            options = it.options,
            isManualInput = it.isManualInput
        )
    }
}
