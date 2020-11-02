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
            var code = jsonObject.getString("code")
            var option = jsonObject.getString("option")
            areas.add(Option(code, option))
        }
        insertArea(areas)
    }

    fun parseStation(station: String?) {
        val jsonArray = JSONArray(station)
        val stations = mutableListOf<Station>()
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            var place = jsonObject.getString("place")
            val stationsJSONArray = jsonObject.getJSONArray("options")
            val options = mutableListOf<Option>()
            for (j in 0 until stationsJSONArray.length()) {
                val jsonArrayObj = stationsJSONArray.getJSONObject(j)
                val optionCode = jsonArrayObj.getString("code")
                val optionValue = jsonArrayObj.getString("option")
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
            val code = jsonObject.getString("code")
            val header = jsonObject.getString("header")
            val question = jsonObject.getString("question")
            val type = jsonObject.getString("type")
            val optionsJSONArray = jsonObject.getJSONArray("options")
            val options = mutableListOf<Option>()
            for (j in 0 until optionsJSONArray.length()) {
                val jsonArrayObj = optionsJSONArray.getJSONObject(j)
                val optionCode = jsonArrayObj.getString("code")
                val optionValue = jsonArrayObj.getString("option")
                options.add(Option(optionCode, optionValue))
            }
            val isManualInput = jsonObject.getInt("isManualInput") > 0
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
            if (it.code == "2" || it.code == "3") {
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

    companion object {
        private const val AREA = "area.json"
        private const val STATION = "radio_stations.json"
        private const val ACTUAL_QUESTIONS = "actual_questions.json"
        private const val Q2 = "Q2"
    }
}
