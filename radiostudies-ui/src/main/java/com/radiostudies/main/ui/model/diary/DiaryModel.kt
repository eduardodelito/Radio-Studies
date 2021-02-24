package com.radiostudies.main.ui.model.diary

import com.radiostudies.main.model.Diary
import java.io.Serializable

/**
 * Created by eduardo.delito on 10/11/20.
 */
data class DiaryModel(
    val panelNumber: String?,
    val memberNumber: String?,
    val nameOfRespondent: String?,
    val dateFrom: String?,
    val dateTo: String?,
    val selectedArea: String?,
    val diary: Diary
) : Serializable
