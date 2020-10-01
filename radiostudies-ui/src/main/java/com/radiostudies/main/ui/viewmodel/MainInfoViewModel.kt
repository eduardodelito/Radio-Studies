package com.radiostudies.main.ui.viewmodel

import com.radiostudies.main.common.livedata.SingleLiveEvent
import com.radiostudies.main.common.viewmodel.BaseViewModel
import com.radiostudies.main.db.manager.MainInfoManager
import com.radiostudies.main.ui.fragment.R
import com.radiostudies.main.ui.mapper.mainInfoModelToMainInfoEntity
import com.radiostudies.main.ui.model.MainInfo
import com.radiostudies.main.ui.model.MainInfoForm
import com.radiostudies.main.ui.model.MainInfoState
import com.radiostudies.main.ui.model.MainInfoValid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class MainInfoViewModel @Inject constructor(private val mainInfoManager: MainInfoManager) :
    BaseViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val mainInfoState = SingleLiveEvent<MainInfoState>()
    internal fun getMainInfoLiveData(): SingleLiveEvent<MainInfoState> = mainInfoState

    fun mainInfoDataChanged(
        panelNumber: String,
        memberNumber: String,
        municipality: String,
        barangay: String,
        nameOfRespondent: String,
        address: String,
        age: String,
        gender: String,
        dateOfInterview: String,
        timeStart: String,
        timeEnd: String,
        dayOfWeek: String,
        contactNumber: String,
        ecoClass: String
    ) {
        if (!isEqualValueValid(panelNumber, 7)) {
            mainInfoState.postValue(MainInfoForm(panelNumber = R.string.panel_number_error))
        } else if (!isEqualValueValid(memberNumber, 2)) {
            mainInfoState.postValue(MainInfoForm(memberNumber = R.string.member_number_error))
        } else if (!isValueValid(municipality, 60)) {
            mainInfoState.postValue(MainInfoForm(municipality = R.string.municipality_error))
        } else if (!isValueValid(barangay, 60)) {
            mainInfoState.postValue(MainInfoForm(barangay = R.string.barangay_error))
        } else if (!isValueValid(nameOfRespondent, 80)) {
            mainInfoState.postValue(MainInfoForm(nameOfRespondent = R.string.name_of_respondent_error))
        } else if (!isValueValid(address, 120)) {
            mainInfoState.postValue(MainInfoForm(address = R.string.address_error))
        } else if (!isValueValid(age, 3)) {
            mainInfoState.postValue(MainInfoForm(age = R.string.address_error))
        } else if (!isValueValid(gender, 6)) {
            mainInfoState.postValue(MainInfoForm(gender = R.string.gender_error))
        } else if (!isValueValid(dateOfInterview, 10)) {
            mainInfoState.postValue(MainInfoForm(dateOfInterview = R.string.date_of_interview_error))
        } else if (!isValueValid(timeStart, 17)) {
            mainInfoState.postValue(MainInfoForm(timeStart = R.string.time_start_error))
        } else if (!isValueValid(timeEnd, 17)) {
            mainInfoState.postValue(MainInfoForm(timeEnd = R.string.time_end_error))
        } else if (!isValueValid(dayOfWeek, 3)) {
            mainInfoState.postValue(MainInfoForm(dayOfWeek = R.string.day_of_week_error))
        } else if (!isValueValid(contactNumber, 20)) {
            mainInfoState.postValue(MainInfoForm(contactNumber = R.string.contact_number_error))
        } else if (!isValueValid(ecoClass, 2)) {
            mainInfoState.postValue(MainInfoForm(ecoClass = R.string.eco_class_error))
        } else {
            insertMainInfo(
                MainInfo(
                    panelNumber,
                    memberNumber,
                    municipality,
                    barangay,
                    nameOfRespondent,
                    address,
                    age,
                    gender,
                    dateOfInterview,
                    timeStart,
                    timeEnd,
                    dayOfWeek,
                    contactNumber,
                    ecoClass
                )
            )
        }
    }

    /**
     * A placeholder value validation check if 1 up to the max value.
     */
    private fun isValueValid(value: String, max: Int): Boolean {
        return value.length in 1..max
    }

    /**
     * A placeholder value validation check if equal to the max value.
     */
    private fun isEqualValueValid(value: String, max: Int): Boolean {
        return value.length == max
    }

    private fun insertMainInfo(mainInfo: MainInfo) {
        launch {
            insertMainInfoToDB(mainInfo)
        }
    }

    private suspend fun insertMainInfoToDB(mainInfo: MainInfo) {
        withContext(Dispatchers.IO) {
            try {
                mainInfoManager.insertMainInfo(mainInfo.mainInfoModelToMainInfoEntity())
                mainInfoState.postValue(MainInfoValid(isSuccess = true))
            } catch (e: Exception) {
                mainInfoState.postValue(MainInfoValid(isSuccess = false))
                e.printStackTrace()
            }
        }
    }
}
