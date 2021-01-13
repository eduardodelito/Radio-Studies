package com.radiostudies.main.ui.model.diary

/**
 * Created by eduardo.delito on 10/18/20.
 */
sealed class DiaryModelState
data class DiaryForm(val list: List<DiaryModel>): DiaryModelState()
