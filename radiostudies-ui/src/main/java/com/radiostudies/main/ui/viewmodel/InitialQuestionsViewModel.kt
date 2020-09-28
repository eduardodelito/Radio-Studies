package com.radiostudies.main.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.radiostudies.main.common.viewmodel.BaseViewModel
import com.radiostudies.main.ui.model.ScreenQuestion
import com.radiostudies.main.ui.model.ScreenQuestionListModel
import com.radiostudies.main.ui.model.ScreenQuestionModel
import com.radiostudies.main.ui.model.ScreenQuestionState
import org.json.JSONArray

class InitialQuestionsViewModel : BaseViewModel() {
    private val screenState = MediatorLiveData<ScreenQuestionState>()
    internal fun getScreenLiveData(): LiveData<ScreenQuestionState> = screenState
    private val list = mutableListOf<ScreenQuestion>()
    private val selectedData = mutableListOf<ScreenQuestion>()

    private var index = -1

    init {
        screenState.postValue(ScreenQuestionModel(SCREEN_QUESTIONS))
    }

    fun parseScreenQuestion(question: String?) {
        list.clear()
        val jsonArray = JSONArray(question)
        for (i in 0 until jsonArray.length()) {
            val item = jsonArray.getJSONObject(i)

            val number = item.getInt("number")
            val questionString = item.getString("question")
            val type = item.getString("type")
            val options = item.getJSONArray("options")
            val actions = item.getJSONArray("action")
            list.add(
                ScreenQuestion(
                    number,
                    questionString,
                    type,
                    parseJSONArray(options),
                    parseJSONArray(actions),
                    type == SINGLE_ANSWER
                )
            )
            selectedData.add(
                ScreenQuestion(
                    number,
                    questionString,
                    type,
                    emptyList(),
                    parseJSONArray(actions),
                    type == SINGLE_ANSWER
                )
            )
        }
        updateNextQuestion()
    }

    fun updateNextQuestion() {
        if (index < list.size - 1) {
            index++
            screenState.postValue(ScreenQuestionListModel(list[index]))
        } else {
            screenState.postValue(ScreenQuestionListModel(isNextScreen = true))
        }
    }

    fun updatePrevQuestion() {
        if (index > -1) {
            index--
            screenState.postValue(ScreenQuestionListModel(list[index]))
        } else {
            index = 1
        }
    }

    private fun getQuestion(index: Int): ScreenQuestion? {
        if (list.size > 0) {
            return list[index]
        }
        return null
    }

    private fun parseJSONArray(jsonArray: JSONArray): List<String> {
        var value = mutableListOf<String>()
        for (i in 0 until jsonArray.length()) {
            value.add(jsonArray.getString(i))
        }
        return value
    }

    fun updateSelectedData(options: List<String>) {
        if (index < selectedData.size - 1) {
            val screenQuestion = selectedData[index]
            selectedData[index] = ScreenQuestion(
                screenQuestion.number,
                screenQuestion.type,
                screenQuestion.question,
                options,
                screenQuestion.actions,
                screenQuestion.isSingleAnswer
            )
        }
    }

    companion object {
        private const val SCREEN_QUESTIONS = "screen_questions.json"
        private const val SINGLE_ANSWER = "Single Answer"
    }
}