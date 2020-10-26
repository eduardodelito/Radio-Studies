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
        dataQuestions = dataQuestions,
        diaries = diaries
    )
}

fun List<DiaryEntity>.diaryEntityModelToDiaryList(): List<Diary> {
    return this.map {
        Diary(
            mainInfo = it.mainInfo,
            dataQuestions = it.dataQuestions,
            diaries = it.diaries
        )
    }
}
