package com.radiostudies.main.model

import java.io.Serializable

/**
 * Created by eduardo.delito on 10/11/20.
 */
data class DiaryModel(
    val userId: String?,
    val panelNumber: String?,
    val memberNumber: String?,
    val nameOfRespondent: String?,
    val dateFrom: String?,
    val dateTo: String?,
    val selectedArea: String?,
    val diary: Diary
) : Serializable

data class DiaryResponse(val status: String?, val code: Int?)
