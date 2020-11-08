package com.radiostudies.main.ui.model.main

import java.io.Serializable

/**
 * Created by eduardo.delito on 10/1/20.
 */
data class MainInfo(
    var panelNumber: String?,
    var memberNumber: String?,
    var municipality: String?,
    var barangay: String? = null,
    var nameOfRespondent: String?,
    var address: String?,
    var age: String?,
    var gender: String?,
    var dateOfInterview: String?,
    var timeStart: String?,
    var timeEnd: String?,
    var dayOfWeek: String?,
    var contactNumber: String?,
    var ecoClass: String?
) : Serializable
