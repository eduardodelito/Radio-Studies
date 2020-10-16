package com.radiostudies.main.ui.model.diary

import java.io.Serializable

/**
 * Created by eduardo.delito on 10/11/20.
 */
data class DiaryModel(
    val panelNumber: String?,
    val memberNumber: String?,
    val nameOfRespondent: String?
) : Serializable
