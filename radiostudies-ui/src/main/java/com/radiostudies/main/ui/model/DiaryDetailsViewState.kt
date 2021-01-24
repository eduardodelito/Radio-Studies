package com.radiostudies.main.ui.model

import com.radiostudies.main.db.entity.Diaries

/**
 * Created by eduardo.delito on 10/26/20.
 */
sealed class DiaryDetailsViewState
data class DiaryDetailsForm(val diaries: List<Diaries>) : DiaryDetailsViewState()