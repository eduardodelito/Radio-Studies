 package com.radiostudies.main.ui.model.diary

import com.radiostudies.main.db.entity.Option

sealed class AddDiaryModelViewState
data class AddDayOfStudyForm(val list: List<Option>): AddDiaryModelViewState()
data class AddDiaryForm(val fileName: String?): AddDiaryModelViewState()
data class StationsForm(val list: List<Option>): AddDiaryModelViewState()
data class TimeOfListeningForm(val list: List<Option>): AddDiaryModelViewState()
data class PlaceOfListeningForm(val list: List<Option>): AddDiaryModelViewState()
data class DeviceForm(val list: List<Option>): AddDiaryModelViewState()
data class CompletedDiaryForm(val isCompleted: Boolean): AddDiaryModelViewState()
