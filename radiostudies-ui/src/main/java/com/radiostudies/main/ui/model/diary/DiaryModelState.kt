package com.radiostudies.main.ui.model.diary

import com.radiostudies.main.db.entity.Option

/**
 * Created by eduardo.delito on 10/18/20.
 */
sealed class DiaryModelState
data class DiaryForm(val list: List<DiaryModel>): DiaryModelState()
data class AddDiaryForm(val fileName: String?): DiaryModelState()
data class StationsForm(val list: List<Option>): DiaryModelState()
data class PlaceOfListeningForm(val list: List<Option>): DiaryModelState()
data class DeviceForm(val list: List<Option>): DiaryModelState()
data class CompletedDiaryForm(val isCompleted: Boolean): DiaryModelState()