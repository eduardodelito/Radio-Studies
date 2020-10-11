package com.radiostudies.main.ui.mapper

import com.radiostudies.main.db.entity.DiaryEntity
import com.radiostudies.main.db.model.Diary

/**
 * Created by eduardo.delito on 10/11/20.
 */
fun Diary.diaryModelToDiaryEntity(): DiaryEntity {
    return DiaryEntity(
        id = 0,
        mainInfo = mainInfo,
        dataQuestions = dataQuestions
    )
}
