package com.radiostudies.main.ui.mapper

import com.radiostudies.main.db.entity.MainInfoEntity
import com.radiostudies.main.ui.model.main.MainInfo

/**
 * Created by eduardo.delito on 10/1/20.
 */
fun MainInfo.mainInfoModelToMainInfoEntity(): MainInfoEntity {
    return MainInfoEntity(
        id = 0,
        panelNumber = panelNumber,
        memberNumber = memberNumber,
        municipality = municipality,
        barangay = barangay,
        nameOfRespondent = nameOfRespondent,
        address = address,
        age = age,
        gender = gender,
        dateOfInterview = dateOfInterview,
        timeStart = timeStart,
        timeEnd = timeEnd,
        dayOfWeek = dayOfWeek,
        contactNumber = contactNumber,
        ecoClass = ecoClass
    )
}
