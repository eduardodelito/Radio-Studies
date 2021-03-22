package com.radiostudies.main.db.model

import com.radiostudies.main.model.Option
import java.io.Serializable

/**
 * Created by eduardo.delito on 10/5/20.
 */
data class ActualQuestion(
    var qId: Int?,
    val code: String?,
    val header: String?,
    var question: String?,
    val type: String?,
    val options: List<Option>,
    val isManualInput: Boolean?
) : Serializable
