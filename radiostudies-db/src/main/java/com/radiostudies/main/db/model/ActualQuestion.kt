package com.radiostudies.main.db.model

import java.io.Serializable

/**
 * Created by eduardo.delito on 10/5/20.
 */
data class ActualQuestion(
    val qId: Int?,
    val code: String?,
    val header: String?,
    val question: String?,
    val type: String?,
    val options: List<String>?,
    val isManualInput: Boolean
) : Serializable