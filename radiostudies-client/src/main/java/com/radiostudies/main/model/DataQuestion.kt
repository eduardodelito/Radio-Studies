package com.radiostudies.main.model

/**
 * Created by eduardo.delito on 10/8/20.
 */
data class DataQuestion(
    var code: String?,
    var header: String?,
    var question: String?,
    var options: List<Option>?
)
