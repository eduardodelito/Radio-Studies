package com.radiostudies.main.ui.viewmodel

import com.radiostudies.main.common.livedata.SingleLiveEvent
import com.radiostudies.main.common.viewmodel.BaseViewModel
import com.radiostudies.main.db.manager.ActualManager
import com.radiostudies.main.db.model.Diary
import com.radiostudies.main.ui.mapper.diaryEntityModelToDiaryList
import com.radiostudies.main.ui.model.diary.DiaryForm
import com.radiostudies.main.ui.model.diary.DiaryModel
import com.radiostudies.main.ui.model.diary.DiaryModelState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class DiaryViewModel @Inject constructor(private val actualManager: ActualManager): BaseViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val diaryState = SingleLiveEvent<DiaryModelState>()
    internal fun getDiaryLiveData(): SingleLiveEvent<DiaryModelState> = diaryState

    fun loadDiary() {
        launch {
            loadDiaryFromDB()
        }
    }

    private suspend fun loadDiaryFromDB() {
        withContext(Dispatchers.IO) {
            try {
                val list = mutableListOf<DiaryModel>()
                val diaryList = actualManager.getDiaryList().diaryEntityModelToDiaryList()
                diaryList.forEach {diary ->
                    list.add(parseMainInfo(diary))
                }
                diaryState.postValue(DiaryForm(list))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun parseMainInfo(diary: Diary): DiaryModel {
        val jsonObject = JSONObject(diary.mainInfo.toString())
        val panelNumber = jsonObject.getString(PANEL_NUMBER)
        val memberNumber = jsonObject.getString(MEMBER_NUMBER)
        val memberName = jsonObject.getString(NAME_OF_RESPONDENT)
        return DiaryModel(panelNumber, memberNumber, memberName)
    }

    companion object {
        private const val PANEL_NUMBER = "panelNumber"
        private const val MEMBER_NUMBER = "memberNumber"
        private const val NAME_OF_RESPONDENT = "nameOfRespondent"
    }
}
