package com.radiostudies.main.ui.viewmodel

import com.google.gson.Gson
import com.radiostudies.main.common.livedata.SingleLiveEvent
import com.radiostudies.main.common.manager.SharedPreferencesManager
import com.radiostudies.main.common.util.getCurrentDateTime
import com.radiostudies.main.common.util.toStringDateTime
import com.radiostudies.main.common.viewmodel.BaseViewModel
import com.radiostudies.main.db.entity.Option
import com.radiostudies.main.db.manager.ActualManager
import com.radiostudies.main.db.model.ActualQuestion
import com.radiostudies.main.db.model.DataQuestion
import com.radiostudies.main.db.model.Diary
import com.radiostudies.main.ui.mapper.*
import com.radiostudies.main.ui.model.actual.*
import com.radiostudies.main.ui.model.main.MainInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ActualQuestionsViewModel @Inject constructor(
    private val actualManager: ActualManager,
    private val sharedPreferencesManager: SharedPreferencesManager
) :
    BaseViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val actualState = SingleLiveEvent<ActualQuestionViewState>()
    internal fun getActualLiveData(): SingleLiveEvent<ActualQuestionViewState> = actualState
    private val actualQuestions = mutableListOf<ActualQuestion>()
    var currentOptions = listOf<Option>()
//    val devicesQuestion = mutableListOf<ActualQuestion>()
//    var deviceIndex = -1

    //    var selectedOptions = mutableListOf<Option>()
    var isSingleAnswer = false
    var selectedArea = ""
    private var id = -1
    private var selectedQId = 0

    fun initViews(genderCode: String?) {
        actualState.postValue(AreaForm(AREA, genderCode))
    }

    fun parseArea(area: String?, genderCode: String?) {
        val jsonArray = JSONArray(area)
        val areas = mutableListOf<Option>()
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val code = jsonObject.getString(CODE)
            val option = jsonObject.getString(OPTION)
            areas.add(Option(code, option))
        }
        insertArea(areas, genderCode)
    }

    fun setClearPanelAndMemberNumber(isClear: Boolean?) {
        sharedPreferencesManager.savePrefs(IS_CLEAR, isClear)
    }

//    fun parseStation(station: String?, genderCode: String?) {
//        val jsonArray = JSONArray(station)
//        val stations = mutableListOf<Station>()
//        for (i in 0 until jsonArray.length()) {
//            val jsonObject = jsonArray.getJSONObject(i)
//            val place = jsonObject.getString(PLACE)
//            val stationsJSONArray = jsonObject.getJSONArray(OPTIONS)
//            val options = mutableListOf<Option>()
//            for (j in 0 until stationsJSONArray.length()) {
//                val jsonArrayObj = stationsJSONArray.getJSONObject(j)
//                val optionCode = jsonArrayObj.getString(CODE)
//                val optionValue = jsonArrayObj.getString(OPTION)
//                options.add(Option(optionCode, optionValue))
//            }
//            stations.add(Station(place, options))
//        }
//        insertStation(stations, genderCode)
//    }

    private fun insertArea(areas: MutableList<Option>, genderCode: String?) {
        launch {
            insertAreaToDB(areas, genderCode)
        }
    }

    private suspend fun insertAreaToDB(areas: MutableList<Option>, genderCode: String?) {
        withContext(Dispatchers.IO) {
            try {
                actualManager.insertArea(areas.areaListModelToAreaListEntity())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        actualState.postValue(ActualQuestionForm(ACTUAL_QUESTIONS, genderCode))
    }

//    private fun insertStation(stations: MutableList<Station>, genderCode: String?) {
//        launch {
//            insertStationToDB(stations, genderCode)
//        }
//    }
//
//    private suspend fun insertStationToDB(stations: MutableList<Station>, genderCode: String?) {
//        withContext(Dispatchers.IO) {
//            try {
//                actualManager.insertStation(stations.optionListModelToStationEntity())
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//        actualState.postValue(ActualQuestionForm(ACTUAL_QUESTIONS, genderCode))
//    }

    fun parseActualQuestions(actual: String?, genderCode: String?) {
        val jsonArray = JSONArray(actual)
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val code = jsonObject.getString(CODE)
            val header = jsonObject.getString(HEADER)
            val question = jsonObject.getString(QUESTION)
            val type = jsonObject.getString(TYPE)
            val optionsJSONArray = jsonObject.getJSONArray(OPTIONS)
            val options = mutableListOf<Option>()
            for (j in 0 until optionsJSONArray.length()) {
                val jsonArrayObj = optionsJSONArray.getJSONObject(j)
                if (code != Q11) {
                    val optionCode = jsonArrayObj.getString(CODE)
                    val optionValue = jsonArrayObj.getString(OPTION)
                    options.add(Option(optionCode, optionValue))
                } else {
                    if (genderCode == CODE_1) {
                        val male = jsonArrayObj.getJSONArray(MALE)
                        parseGender(male, options)
                    } else {
                        val female = jsonArrayObj.getJSONArray(FEMALE)
                        parseGender(female, options)
                    }
                }
            }
            val isManualInput = jsonObject.getInt(IS_MANUAL_INPUT) > 0

            if (!jsonObject.isNull(GENDER_CODE)) {
                val mGenderCode = jsonObject.getString(GENDER_CODE)
                if (mGenderCode == genderCode) {
                    actualQuestions.add(
                        ActualQuestion(
                            selectedQId,
                            code,
                            header,
                            question,
                            type,
                            options,
                            isManualInput
                        )
                    )
                    selectedQId++
                }
            } else {
                actualQuestions.add(
                    ActualQuestion(
                        selectedQId,
                        code,
                        header,
                        question,
                        type,
                        options,
                        isManualInput
                    )
                )
                selectedQId++
            }
        }

        insertActualQuestion(actualQuestions)
    }

    private fun parseGender(jsonArray: JSONArray, options: MutableList<Option>) {
        for (i in 0 until jsonArray.length()) {
            val jsonArrayObj = jsonArray.getJSONObject(i)
            val optionCode = jsonArrayObj.getString(CODE)
            val optionValue = jsonArrayObj.getString(OPTION)
            options.add(Option(optionCode, optionValue))
        }
    }

    private fun insertActualQuestion(actualQuestions: List<ActualQuestion>) {
        launch {
            insertActualQuestionToDB(actualQuestions)
        }
    }

    private suspend fun insertActualQuestionToDB(actualQuestions: List<ActualQuestion>) {
        withContext(Dispatchers.IO) {
            try {
                actualManager.insertActualQuestion(actualQuestions.actualQuestionListModelToActualQuestionEntity())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        queryActualQuestion(plus(), true)
    }

    fun plus(): Int {
        id++
        if (id > (actualQuestions.size - 1)) {
            id = actualQuestions.size - 1
        }
        return id
    }

    fun minus(): Int {
        id--
        if (id <= 0) {
            id = 0
        }
        return id
    }

    fun isEndOfQuestion() = id == (actualQuestions.size - 1)

    fun queryActualQuestion(qId: Int, isPlus: Boolean) {
        launch {
            queryActualQuestionFromDB(qId, isPlus)
        }
    }

    fun loadAreas() {
        launch {
            loadAreasFromDB()
        }

    }

    private suspend fun loadAreasFromDB() {
        withContext(Dispatchers.IO) {
            try {
                currentOptions = actualManager.queryAreas()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadStations(station: String?) {
        val jsonArray = JSONArray(station)
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val place = jsonObject.getString(PLACE)
            if (place == selectedArea) {
                val stationsJSONArray = jsonObject.getJSONArray(OPTIONS)
                val options = mutableListOf<Option>()
                for (j in 0 until stationsJSONArray.length()) {
                    val jsonArrayObj = stationsJSONArray.getJSONObject(j)
                    val optionCode = jsonArrayObj.getString(CODE)
                    val optionValue = jsonArrayObj.getString(OPTION)
                    options.add(Option(optionCode, optionValue))
                }
                currentOptions = options
            }
        }
    }

//    private suspend fun loadStationsFromDB(station: String?) {
//        withContext(Dispatchers.IO) {
//            try {
//                val newOptions = mutableListOf<Option>()
//                currentOptions = actualManager.getSelectedArea(selectedArea)
//                currentOptions.forEach {
//                    if (!it.option.contains(station.toString()))
//                        newOptions.add(it)
//                }
//                currentOptions = newOptions
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }

    private suspend fun queryActualQuestionFromDB(qId: Int, isPlus: Boolean) {
        withContext(Dispatchers.IO) {
            try {
                var actualQuestion: ActualQuestion?
                    val dataQuestion = actualManager.queryDataQuestion(Q2)?.dataQuestionEntityToDataQuestionModel()
                    val dataQuestionQ17 = actualManager.queryDataQuestion(Q17)?.dataQuestionEntityToDataQuestionModel()
                    val dataQuestionQ18 = actualManager.queryDataQuestion(Q18)?.dataQuestionEntityToDataQuestionModel()
                    actualQuestion = if (dataQuestion != null && !optionsHasRent(dataQuestion.options) && id == 2) {
                        if (isPlus) {
                            plus()
                        } else {
                            minus()
                        }
                        actualManager.queryQuestion(id)
                    } else {
                        actualManager.queryQuestion(qId)
                    }

                    if (dataQuestionQ17 != null && actualQuestion.code == Q19A && optionsNeverListen(dataQuestionQ17.options)) {
                        if (isPlus) {
                            plus()
                        } else {
                            minus()
                        }
                        actualQuestion = actualManager.queryQuestion(id)
                        if(dataQuestionQ18 != null && actualQuestion.code == Q19B && optionsNeverListen(dataQuestionQ18.options)) {
                            if (isPlus) {
                                plus()
                            } else {
                                minus()
                            }
                            actualQuestion = actualManager.queryQuestion(id)
                        }
                    } else if(dataQuestionQ18 != null && actualQuestion.code == Q19B && optionsNeverListen(dataQuestionQ18.options)) {
                        if (isPlus) {
                            plus()
                        } else {
                            minus()
                        }
                        actualQuestion = actualManager.queryQuestion(id)
                        if(dataQuestionQ17 != null && actualQuestion.code == Q19A && optionsNeverListen(dataQuestionQ17.options)) {
                            if (isPlus) {
                                plus()
                            } else {
                                minus()
                            }
                            actualQuestion = actualManager.queryQuestion(id)
                        }
                    }

                actualState.postValue(
                    ActualQuestionModel(
                        actualQuestion,
                        id != 0,
                        false,
                        isPlus
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun validateCode(codeStr: String, panelNumber: String?) {
        val code = arrayOf(
            Q1, Q2, Q2A, Q3, Q4, Q5a, Q5b, Q6, Q7,
            Q26, Q27a, Q27b, Q27c, Q27d, Q27e, Q27f, Q27g, Q28, Q29
        )
        if (code.contains(codeStr)) {
            launch {
                queryOptions(codeStr, panelNumber)
            }
        }
    }

    private suspend fun queryOptions(codeStr: String, panelNumber: String?) {
        withContext(Dispatchers.IO) {
            try {
                if (actualManager.isPanelNUmberExist(panelNumber)) {
                    val diary = actualManager.queryDiary(panelNumber)
                    val dataQuestion = diary?.dataQuestions?.find { it.code == codeStr }
                    actualState.postValue(dataQuestion?.options?.let { LoadOptions(it) })
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun optionsHasRent(options: List<Option>?): Boolean {
        options?.forEach {
            if (it.code == CODE_3) {
                return true
            }
        }
        return false
    }

    private fun optionsNeverListen(options: List<Option>?): Boolean {
        options?.forEach {
            if (it.code == CODE_5) {
                return true
            }
        }
        return false
    }

    fun saveActualQuestions(
        mainInfo: MainInfo?
    ) {
        setClearPanelAndMemberNumber(false)
        launch {
            saveActualQuestionsIntoDB(mainInfo)
        }
    }

    private suspend fun saveActualQuestionsIntoDB(
        mainInfo: MainInfo?
    ) {
        withContext(Dispatchers.IO) {
            try {
                val list = actualManager.queryDataQuestions().dataQuestionsEntityToDataQuestions()
                mainInfo?.timeEnd = getTime()
                val mainInfoString = Gson().toJson(mainInfo)
                val diary = Diary(
                    mainInfo?.panelNumber,
                    mainInfo?.memberNumber,
                    mainInfoString,
                    list,
                    mutableListOf()
                )
                actualManager.saveCompletedActualQuestions(
                    diary.diaryModelToDiaryEntity()
                )
                actualManager.deleteSaveDataQuestions()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        actualState.postValue(ActualQuestionComplete(true))
    }

    fun saveQuestion(dataQuestion: DataQuestion) {
        launch {
            saveQuestionIntoDB(dataQuestion)
        }
    }

    private suspend fun saveQuestionIntoDB(dataQuestion: DataQuestion) {
        withContext(Dispatchers.IO) {
            try {
                actualManager.saveDataQuestion(dataQuestion.dataQuestionModelToDataQuestionEntity())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun querySelectedAnswer(code: String?) {
        launch {
            querySelectedAnswerFromDB(code)
        }
    }

    private suspend fun querySelectedAnswerFromDB(code: String?) {
        withContext(Dispatchers.IO) {
            try {
                val selectedOptions = actualManager.queryDataQuestion(code)?.options as MutableList
                actualState.postValue(SelectedOptionForm(selectedOptions))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getTime() = getCurrentDateTime(null, null,0).toStringDateTime(TIME_FORMAT)

    companion object {
        private const val AREA = "area.json"
        private const val ACTUAL_QUESTIONS = "actual_questions.json"
        private const val Q1 = "Q1"
        private const val Q2 = "Q2"
        private const val Q2A = "Q2a"
        private const val Q3 = "Q3"
        private const val Q4 = "Q4"
        private const val Q5a = "Q5a"
        private const val Q5b = "Q5b"
        private const val Q6 = "Q6"
        private const val Q7 = "Q7"
        private const val Q11 = "Q11"
        private const val Q17 = "Q17"
        private const val Q18 = "Q18"
        private const val Q19A = "Q19a"
        private const val Q19B = "Q19b"
        private const val Q26 = "Q26"
        private const val Q27a = "Q27a"
        private const val Q27b = "Q27b"
        private const val Q27c = "Q27c"
        private const val Q27d = "Q27d"
        private const val Q27e = "Q27e"
        private const val Q27f = "Q27f"
        private const val Q27g = "Q27g"
        private const val Q28 = "Q28"
        private const val Q29 = "Q29"
        private const val CODE_1 = "1"
        private const val CODE_3 = "3"
        private const val CODE_5 = "5"
        private const val PLACE = "place"
        private const val HEADER = "header"
//        private const val CHILD_QUESTIONS = "childQuestions"
        private const val QUESTION = "question"
        private const val TYPE = "type"
        private const val GENDER_CODE = "genderCode"
        private const val OPTIONS = "options"
        private const val CODE = "code"
        private const val OPTION = "option"
        private const val MALE = "male"
        private const val FEMALE = "female"
        private const val IS_MANUAL_INPUT = "isManualInput"
        private const val TIME_FORMAT = "hh:mm:ss a"

        private const val IS_CLEAR = "is_clear"
    }
}

