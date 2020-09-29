package com.radiostudies.main.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.radiostudies.main.common.viewmodel.BaseViewModel
import com.radiostudies.main.db.manager.QuestionManager
import com.radiostudies.main.ui.mapper.questionModelToQuestionEntity
import com.radiostudies.main.ui.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class InitialQuestionsViewModel @Inject constructor(private val questionManager: QuestionManager) :
    BaseViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val screenState = MediatorLiveData<ScreenQuestionState>()
    internal fun getScreenLiveData(): LiveData<ScreenQuestionState> = screenState
    private val list = mutableListOf<ScreenQuestion>()

    var index = -1
    var currentQuestion: String? = null

    init {
        screenState.postValue(ScreenQuestionModel(SCREEN_QUESTIONS))
    }

    fun parseScreenQuestion(question: String?) {
        list.clear()
        val jsonArray = JSONArray(question)
        val questions = mutableListOf<Question>()
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

            questions.add(Question(questionString, ""))
        }
        insertQuestions(questions)
        updateNextQuestion()
    }

    fun updateNextQuestion() {
        index++
        if (index < list.size) {
            screenState.postValue(ScreenQuestionListModel(list[index]))
        } else {
            index = 5
        }
    }

    fun updatePrevQuestion() {
        index--
        println("index0=============$index")
        if (index > -1) {
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

    private fun insertQuestions(questions: List<Question>) {
        launch {
            insertInitialQuestions(questions)
        }
    }

    private suspend fun insertInitialQuestions(questions: List<Question>) {
        withContext(Dispatchers.IO) {
            try {
                questionManager.insertQuestions(questions.questionModelToQuestionEntity())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateQuestion(answers: MutableList<String>) {
        launch {
            updateInitialQuestion(currentQuestion, answers.toString())
        }
    }

    private suspend fun updateInitialQuestion(question: String?, answers: String?) {
        withContext(Dispatchers.IO) {
            try {
                questionManager.updateQuestion(question, answers)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        private const val SCREEN_QUESTIONS = "screen_questions.json"
        private const val SINGLE_ANSWER = "Single Answer"
    }
}