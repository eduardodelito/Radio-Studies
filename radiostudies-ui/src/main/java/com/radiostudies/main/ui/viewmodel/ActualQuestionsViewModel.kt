package com.radiostudies.main.ui.viewmodel

import com.radiostudies.main.common.livedata.SingleLiveEvent
import com.radiostudies.main.common.viewmodel.BaseViewModel
import com.radiostudies.main.db.entity.Option
import com.radiostudies.main.db.manager.ActualManager
import com.radiostudies.main.db.manager.QuestionManager
import com.radiostudies.main.db.model.ActualQuestion
import com.radiostudies.main.db.model.DataQuestion
import com.radiostudies.main.db.model.Diary
import com.radiostudies.main.ui.mapper.*
import com.radiostudies.main.ui.model.actual.*
import com.radiostudies.main.ui.model.station.Station
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ActualQuestionsViewModel @Inject constructor(
    private val questionManager: QuestionManager,
    private val actualManager: ActualManager
) :
    BaseViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val actualState = SingleLiveEvent<ActualQuestionState>()
    internal fun getActualLiveData(): SingleLiveEvent<ActualQuestionState> = actualState
    private val actualQuestions = mutableListOf<ActualQuestion>()
    var currentOptions = listOf<Option>()
    val devicesQuestion = mutableListOf<ActualQuestion>()
    var deviceIndex = -1

    //    var selectedOptions = mutableListOf<Option>()
    var isSingleAnswer = false
    var selectedArea = ""
    private var id = -1

    init {
        actualState.postValue(AreaForm(AREA))
    }

    fun parseArea(area: String?) {
        val jsonArray = JSONArray(area)
        val areas = mutableListOf<Option>()
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            var code = jsonObject.getString(CODE)
            var option = jsonObject.getString(OPTION)
            areas.add(Option(code, option))
        }
        insertArea(areas)
    }

    fun parseStation(station: String?) {
        val jsonArray = JSONArray(station)
        val stations = mutableListOf<Station>()
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            var place = jsonObject.getString(PLACE)
            val stationsJSONArray = jsonObject.getJSONArray(OPTIONS)
            val options = mutableListOf<Option>()
            for (j in 0 until stationsJSONArray.length()) {
                val jsonArrayObj = stationsJSONArray.getJSONObject(j)
                val optionCode = jsonArrayObj.getString(CODE)
                val optionValue = jsonArrayObj.getString(OPTION)
                options.add(Option(optionCode, optionValue))
            }
            stations.add(Station(place, options))
        }
        insertStation(stations)
    }

    private fun insertArea(areas: MutableList<Option>) {
        launch {
            insertAreaToDB(areas)
        }
    }

    private suspend fun insertAreaToDB(areas: MutableList<Option>) {
        withContext(Dispatchers.IO) {
            try {
                actualManager.insertArea(areas.areaListModelToAreaListEntity())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        actualState.postValue(StationForm(STATION))
    }

    private fun insertStation(stations: MutableList<Station>) {
        launch {
            insertStationToDB(stations)
        }
    }

    private suspend fun insertStationToDB(stations: MutableList<Station>) {
        withContext(Dispatchers.IO) {
            try {
                actualManager.insertStation(stations.optionListModelToStationEntity())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        actualState.postValue(ActualQuestionForm(ACTUAL_QUESTIONS))
    }

    fun parseActualQuestions(actual: String?) {
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
                val optionCode = jsonArrayObj.getString(CODE)
                val optionValue = jsonArrayObj.getString(OPTION)
                options.add(Option(optionCode, optionValue))
            }
            val isManualInput = jsonObject.getInt(IS_MANUAL_INPUT) > 0
            actualQuestions.add(
                ActualQuestion(
                    i,
                    code,
                    header,
                    question,
                    type,
                    options,
                    isManualInput
                )
            )
        }

        insertActualQuestion(actualQuestions)
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

    fun loadStations() {
        launch {
            loadStationsFromDB()
        }
    }

    private suspend fun loadStationsFromDB() {
        withContext(Dispatchers.IO) {
            try {
                currentOptions = actualManager.getSelectedArea(selectedArea)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun queryActualQuestionFromDB(qId: Int, isPlus: Boolean) {
        withContext(Dispatchers.IO) {
            try {
                var actualQuestion: ActualQuestion?

                if (devicesQuestion.size > 0 && deviceIndex < (devicesQuestion.size - 1)) {
                    deviceIndex++
                    actualQuestion = devicesQuestion[deviceIndex]
                    id = 5
                } else {
                    val dataQuestion =
                        actualManager.queryDataQuestion(Q2)?.dataQuestionEntityToDataQuestionModel()
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
                }

                actualState.postValue(
                    ActualQuestionModel(
                        actualQuestion,
                        id != 0,
                        false
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun optionsHasRent(options: List<Option>?): Boolean {
        options?.forEach {
            if (it.code == CODE_2 || it.code == CODE_3) {
                return true
            }
        }
        return false
    }

    fun saveActualQuestions(
        mainInfo: String?
    ) {
        launch {
            saveActualQuestionsIntoDB(mainInfo)
        }
    }

    private suspend fun saveActualQuestionsIntoDB(
        mainInfo: String?
    ) {
        withContext(Dispatchers.IO) {
            try {
                var list = actualManager.queryDataQuestions().dataQuestionsEntityToDataQuestions()
                actualManager.saveCompletedActualQuestions(
                    Diary(
                        mainInfo,
                        list,
                        mutableListOf()
                    ).diaryModelToDiaryEntity()
                )
                questionManager.deleteQuestion()
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

    fun parseDevice(device: String?, selectedDevice: String?) {
        val jsonArray = JSONArray(device)
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
//            var code = jsonObject.getString("code")
            var option = jsonObject.getString(OPTION)
            if (option == selectedDevice) {
                var childQuestionsArr = jsonObject.getJSONArray(CHILD_QUESTIONS)
                for (j in 0 until childQuestionsArr.length()) {
                    val childQuestionsJObj = childQuestionsArr.getJSONObject(j)
                    var childCode = childQuestionsJObj.getString(CODE)
                    var childHeader = childQuestionsJObj.getString(HEADER)
                    var childQuestion = childQuestionsJObj.getString(QUESTION)
                    var childType = childQuestionsJObj.getString(TYPE)
                    val childOptions = mutableListOf<Option>()
                    var childOptionsArr = childQuestionsJObj.getJSONArray(OPTIONS)
                    for (k in 0 until childOptionsArr.length()) {
                        val childOptionsJObj = childOptionsArr.getJSONObject(k)
                        var subChildCode = childOptionsJObj.getString(CODE)
                        var subChildOption = childOptionsJObj.getString(OPTION)
                        childOptions.add(Option(subChildCode, subChildOption))
                    }
                    devicesQuestion.add(ActualQuestion(i, childCode, childHeader, childQuestion, childType, childOptions, false))
                }
            }
        }
    }

    companion object {
        private const val AREA = "area.json"
        private const val STATION = "radio_stations.json"
        private const val ACTUAL_QUESTIONS = "actual_questions.json"
        private const val Q2 = "Q2"
        private const val CODE_2 = "2"
        private const val CODE_3 = "3"
        private const val PLACE = "place"
        private const val HEADER = "header"
        private const val CHILD_QUESTIONS = "childQuestions"
        private const val QUESTION = "question"
        private const val TYPE = "type"
        private const val OPTIONS = "options"
        private const val CODE = "code"
        private const val OPTION = "option"
        private const val IS_MANUAL_INPUT = "isManualInput"
    }
}
