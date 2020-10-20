package com.radiostudies.main.ui.viewmodel

import com.radiostudies.main.common.livedata.SingleLiveEvent
import com.radiostudies.main.common.viewmodel.BaseViewModel
import com.radiostudies.main.db.entity.Option
import com.radiostudies.main.ui.model.diary.AddDiaryForm
import com.radiostudies.main.ui.model.diary.DiaryModelState
import org.json.JSONArray

class AddDiaryViewModel : BaseViewModel() {

    private val diaryState = SingleLiveEvent<DiaryModelState>()
    internal fun getDiaryLiveData(): SingleLiveEvent<DiaryModelState> = diaryState

    var timeOfListeningList = mutableListOf<Option>()
    init {
        diaryState.postValue(AddDiaryForm(TIME_OF_LISTENING))
    }

    fun parseTimeOfListening(value: String?) {
        val jsonArray = JSONArray(value)
        for (i in 0 until jsonArray.length()) {
            val jObj = jsonArray.getJSONObject(i)
            val code = jObj.getString("code")
            val option = jObj.getString("value")
            timeOfListeningList.add(Option(code, option))
        }
    }

    companion object {
        private const val TIME_OF_LISTENING = "time_of_listening.json"
    }
}
