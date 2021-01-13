package com.radiostudies.main.ui.model.diary

import com.radiostudies.main.db.entity.Option

sealed class AddDiaryModelState
data class AddDayOfStudyForm(val list: List<Option>): AddDiaryModelState()
data class AddDiaryForm(val fileName: String?): AddDiaryModelState()
data class StationsForm(val list: List<Option>): AddDiaryModelState()
data class TimeOfListeningForm(val list: List<Option>): AddDiaryModelState()
data class PlaceOfListeningForm(val list: List<Option>): AddDiaryModelState()
data class DeviceForm(val list: List<Option>): AddDiaryModelState()
data class CompletedDiaryForm(val isCompleted: Boolean): AddDiaryModelState()
