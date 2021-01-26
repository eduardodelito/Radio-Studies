package com.radiostudies.main.ui.model.main

/**
 * Created by eduardo.delito on 9/29/20.
 */
sealed class MainInfoViewState
data class MainInfoForm(
    val panelNumber: Int? = null,
    val memberNumber: Int? = null,
    val municipality: Int? = null,
    val barangay: Int? = null,
    val nameOfRespondent: Int? = null,
    val address: Int? = null,
    val age: Int? = null,
    val gender: Int? = null,
    val dateOfInterview: Int? = null,
    val timeStart: Int? = null,
    val timeEnd: Int? = null,
    val dayOfWeek: Int? = null,
    val contactNumber: Int? = null,
    val ecoClass: Int? = null
) :
    MainInfoViewState()

data class MainInfoData(val mainInfo: MainInfo?) : MainInfoViewState()

data class MainInfoErrorMessage(val msg: Int) : MainInfoViewState()

data class LoadMainInfoForm(val mainInfo: MainInfo?) : MainInfoViewState()

data class ClearMainInfo(val isClear: Boolean) : MainInfoViewState()
