package com.radiostudies.main.db.model

/**
 * Created by eduardo.delito on 10/11/20.
 */
data class Diary(
    var panelNumber: String?,
    var memberNumber: String?,
    var date: String?,
    var dataQuestions: List<DataQuestion>?
)