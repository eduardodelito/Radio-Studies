package com.radiostudies.main.ui.viewmodel

import com.radiostudies.main.common.livedata.SingleLiveEvent
import com.radiostudies.main.common.viewmodel.BaseViewModel
import com.radiostudies.main.db.entity.Diaries
import com.radiostudies.main.db.manager.ActualManager
import com.radiostudies.main.db.model.Diary
import com.radiostudies.main.ui.mapper.diaryModelToDiaryEntity
import com.radiostudies.main.ui.model.DiaryDetailsForm
import com.radiostudies.main.ui.model.DiaryDetailsViewState
import com.radiostudies.main.ui.model.diary.DiaryModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class DiaryDetailsViewModel @Inject constructor(private val actualManager: ActualManager) : BaseViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val diaryDetailsState = SingleLiveEvent<DiaryDetailsViewState>()
    internal fun getDiaryDetailsLiveData(): SingleLiveEvent<DiaryDetailsViewState> = diaryDetailsState

    var diaryModel: DiaryModel?= null

    fun loadDiaries(mainInfo: String) {
        launch {
            diaries(mainInfo)
        }
    }

    private suspend fun diaries(mainInfo: String) {
        withContext(Dispatchers.IO) {
            try {
                val diaries = actualManager.getDiaries(mainInfo) as List<Diaries>
                diaryDetailsState.postValue(DiaryDetailsForm(diaries))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteSelectedDiaries(diary: Diary, diaryList: List<Diaries>, diaries: Diaries) {
        val newList = mutableListOf<Diaries>()
        diaryList.forEach { mDiaries ->
            if (diaries != mDiaries) {
                newList.add(mDiaries)
            }
        }
        updateDiaries(diary, newList)
    }

    private fun updateDiaries(diary: Diary, newList: List<Diaries>) {
        launch {
            updateSelectedDiaries(diary, newList)
        }
    }

    private suspend fun updateSelectedDiaries(diary: Diary, newList: List<Diaries>) {
        withContext(Dispatchers.IO) {
            try {
                diary.diaries = newList
                actualManager.updateDiary(diary.diaryModelToDiaryEntity())
                diaryDetailsState.postValue(DiaryDetailsForm(newList))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
