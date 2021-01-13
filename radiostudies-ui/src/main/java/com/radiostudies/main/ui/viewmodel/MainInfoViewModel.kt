package com.radiostudies.main.ui.viewmodel

import com.radiostudies.main.common.livedata.SingleLiveEvent
import com.radiostudies.main.common.util.getCurrentDateTime
import com.radiostudies.main.common.util.toStringDateTime
import com.radiostudies.main.common.viewmodel.BaseViewModel
import com.radiostudies.main.db.manager.ActualManager
import com.radiostudies.main.ui.fragment.R
import com.radiostudies.main.ui.model.main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class MainInfoViewModel @Inject constructor(private val actualManager: ActualManager) :
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
        } else if (!isValueValid(gender, 1)) {
            mainInfoState.postValue(MainInfoForm(gender = R.string.gender_error))
        } else if (contactNumber.length != 11) {
            mainInfoState.postValue(MainInfoForm(contactNumber = R.string.contact_number_error))
        } else if (!isValueValid(ecoClass, 1)) {
            mainInfoState.postValue(MainInfoForm(ecoClass = R.string.eco_class_error))
        } else {
                val mainInfo = MainInfo(
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
                    EMPTY_STRING,
                    dayOfWeek,
                    contactNumber,
                    ecoClass
                )
            validateMainInfo(mainInfo)
        }
    }

    private fun validateMainInfo(mainInfo: MainInfo) {
        launch {
            validateMainInfoFromDB(mainInfo)
        }
    }

    private suspend fun validateMainInfoFromDB(mainInfo: MainInfo) {
        withContext(Dispatchers.IO) {
            try {
                if (!actualManager.validateMainInfo(mainInfo.panelNumber, mainInfo.memberNumber)) {
                    mainInfoState.postValue(MainInfoData(mainInfo))
                } else {
                    mainInfoState.postValue(MainInfoErrorMessage(R.string.main_info_exist_msg))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                mainInfoState.postValue(MainInfoErrorMessage(R.string.main_info_exist_msg))
            }
        }
    }

    fun clearInfoAfterActualQuestionCompleted(panelNumber: String, memberNumber: String) {
        launch {
            validateMainInfoIfCompletedFromDB(panelNumber, memberNumber)
        }
    }

    private suspend fun validateMainInfoIfCompletedFromDB(panelNumber: String, memberNumber: String) {
        withContext(Dispatchers.IO) {
          try {
              mainInfoState.postValue(ClearMainInfo(actualManager.validateMainInfo(panelNumber, memberNumber)))
          } catch (e: Exception) {
              e.printStackTrace()
          }
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

//    private fun insertMainInfo(mainInfo: MainInfo) {
//        launch {
//            insertMainInfoToDB(mainInfo)
//        }
//    }
//
//    private suspend fun insertMainInfoToDB(mainInfo: MainInfo) {
//        withContext(Dispatchers.IO) {
//            try {
//                mainInfoManager.insertMainInfo(mainInfo.mainInfoModelToMainInfoEntity())
//                mainInfoState.postValue(MainInfoValid(isSuccess = true))
//            } catch (e: Exception) {
//                mainInfoState.postValue(MainInfoValid(isSuccess = false))
//                e.printStackTrace()
//            }
//        }
//    }

    fun getDate() = getCurrentDateTime(null, null, 0).toStringDateTime(DATE_FORMAT)

    fun getTime() = getCurrentDateTime(null, null, 0).toStringDateTime(TIME_FORMAT)

    fun getDay() = getCurrentDateTime(null, null, 0).toStringDateTime(DAY_FORMAT)

    fun getCode(): Int {
        val dayOfWeek= listOf(MON, TUE, WED, THU, FRI, SAT, SUN)
        for (i in dayOfWeek.indices) {
            if (dayOfWeek[i] == getDay()) {
                return (i + 1)
            }
        }
        return 0
    }

    companion object {
        private const val DATE_FORMAT = "MMM/dd/yyyy"
        private const val TIME_FORMAT = "hh:mm:ss a"
        private const val DAY_FORMAT = "EEE"

        private const val MON = "Mon"
        private const val TUE = "Tue"
        private const val WED = "Wed"
        private const val THU = "Thu"
        private const val FRI = "Fri"
        private const val SAT = "Sat"
        private const val SUN = "Sun"

        private const val EMPTY_STRING = ""
    }
}
