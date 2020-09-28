package com.radiostudies.main.ui.model

/**
 * Created by eduardo.delito on 9/19/20.
 */
data class ScreenQuestion(
    val number: Int,
    val question: String?,
    val type: String?,
    val options: List<String>?,
    val actions: List<String>?,
    val isSingleAnswer: Boolean?
)
