package com.radiostudies.main.ui.model.diary

/**
 * Created by eduardo.delito on 10/11/20.
 */
sealed class DiaryModelState
data class DiaryForm(val diaryList: List<DiaryModel>): DiaryModelState()