package com.radiostudies.main.model

import java.io.Serializable

/**
 * Created by eduardo.delito on 10/11/20.
 */
data class Diary(
    var userId: String?,
    var panelNumber: String?,
    var memberNumber: String?,
    var mainInfo: String?,
    var dataQuestions: List<DataQuestion>?,
    var diaries: List<Diaries>?
): Serializable

data class Diaries(
    val dayOfStudy: List<Option>,
    val diaryDate: List<Option>,
    val timeOfListening: List<Option>,
    val stations: List<Option>,
    val placeOfListening: List<Option>,
    val device: List<Option>
)

data class Option(var code: String, var option: String)
