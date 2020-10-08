package com.radiostudies.main.ui.viewmodel

import com.radiostudies.main.common.livedata.SingleLiveEvent
import com.radiostudies.main.common.viewmodel.BaseViewModel
import com.radiostudies.main.db.manager.ActualManager
import com.radiostudies.main.db.model.ActualQuestion
import com.radiostudies.main.db.model.Area
import com.radiostudies.main.ui.mapper.actualQuestionListModelToActualQuestionEntity
import com.radiostudies.main.ui.mapper.areaListModelToAreaListEntity
import com.radiostudies.main.ui.model.actual.ActualQuestionForm
import com.radiostudies.main.ui.model.actual.ActualQuestionModel
import com.radiostudies.main.ui.model.actual.ActualQuestionState
import com.radiostudies.main.ui.model.actual.AreaForm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ActualQuestionsViewModel @Inject constructor(private val actualManager: ActualManager) :
    BaseViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val actualState = SingleLiveEvent<ActualQuestionState>()
    internal fun getActualLiveData(): SingleLiveEvent<ActualQuestionState> = actualState
    private val actualQuestions = mutableListOf<ActualQuestion>()
    var currentOptions = listOf<String>()
    var currentAreas = listOf<Area>()
    var isSingleAnswer = false
    private var id = -1

    init {
        actualState.postValue(AreaForm(AREA))
    }

    fun parseArea(area: String?) {
        val jsonArray = JSONArray(area)
        val areas = mutableListOf<Area>()
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            var code = jsonObject.getString("code")
            var place = jsonObject.getString("place")
            var isManualInput = jsonObject.getInt("isManualInput")
            areas.add(Area(code, place, isManualInput))
        }
        insertArea(areas)
    }

    private fun insertArea(areas: MutableList<Area>) {
        launch {
            insertAreaToDB(areas)
        }
    }

    private suspend fun insertAreaToDB(areas: MutableList<Area>) {
        withContext(Dispatchers.IO) {
            try {
                actualManager.insertArea(areas.areaListModelToAreaListEntity())
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
            val options = mutableListOf<String>()
            for (j in 0 until optionsJSONArray.length()) {
                options.add(optionsJSONArray.getString(j))
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
        queryActualQuestion(plus())
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

    fun queryActualQuestion(qId: Int) {
        launch {
            queryActualQuestionFromDB(qId)
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

    private suspend fun queryActualQuestionFromDB(qId: Int) {
        withContext(Dispatchers.IO) {
            try {
                actualState.postValue(
                    ActualQuestionModel(
                        actualManager.queryQuestion(qId),
                        id != 0,
                        false
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        private const val AREA = "area.json"
        private const val ACTUAL_QUESTIONS = "actual_questions.json"
        private const val AREA_LABEL = "Area"
    }
}
