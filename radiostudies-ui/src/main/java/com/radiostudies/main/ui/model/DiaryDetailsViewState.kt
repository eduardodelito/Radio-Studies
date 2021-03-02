package com.radiostudies.main.ui.model

import com.radiostudies.main.model.Diaries
import com.radiostudies.main.model.DiaryResponse

/**
 * Created by eduardo.delito on 10/26/20.
 */
sealed class DiaryDetailsViewState
data class DiaryDetailsForm(val diaries: List<Diaries>) : DiaryDetailsViewState()
data class DiarySubmitForm(val complete: Boolean?, val diaryResponse: DiaryResponse, val mainInfo: String? = null) : DiaryDetailsViewState()
data class DeleteForm(val isDeleted: Boolean?) : DiaryDetailsViewState()