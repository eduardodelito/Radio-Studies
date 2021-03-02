package com.radiostudies.main.ui.viewmodel

import com.radiostudies.main.common.livedata.SingleLiveEvent
import com.radiostudies.main.common.viewmodel.BaseViewModel
import com.radiostudies.main.db.manager.ActualManager
import com.radiostudies.main.model.Diaries
import com.radiostudies.main.model.Diary
import com.radiostudies.main.model.DiaryModel
import com.radiostudies.main.model.DiaryResponse
import com.radiostudies.main.repository.DiariesRepository
import com.radiostudies.main.ui.mapper.diaryModelToDiaryEntity
import com.radiostudies.main.ui.model.DeleteForm
import com.radiostudies.main.ui.model.DiaryDetailsForm
import com.radiostudies.main.ui.model.DiaryDetailsViewState
import com.radiostudies.main.ui.model.DiarySubmitForm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class DiaryDetailsViewModel @Inject constructor(
    private val actualManager: ActualManager,
    private val diariesRepository: DiariesRepository
) : BaseViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val diaryDetailsState = SingleLiveEvent<DiaryDetailsViewState>()
    internal fun getDiaryDetailsLiveData(): SingleLiveEvent<DiaryDetailsViewState> =
        diaryDetailsState

    var diaryModel: DiaryModel? = null

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

    fun sendDiaries(diaryModel: DiaryModel?) {
        val diaries = diaryModel?.diary?.diaries
        val value = diaries?.find { selectedDiaries -> selectedDiaries.dayOfStudy[0].option == DAY_7 }
        if (diaries?.size == 7 && value != null) {
            launch {
                sendDiariesToService(diaryModel)
            }
        } else {
            diaryDetailsState.postValue(
                DiarySubmitForm(
                    false, DiaryResponse(
                        "Incomplete diary.",
                        500
                    )
                )
            )
        }
    }

    private suspend fun sendDiariesToService(diaryModel: DiaryModel?) {
        withContext(Dispatchers.IO) {
            try {
                diaryModel?.let {
                    val diaryResponse = diariesRepository.sendDiary(it.diary)
                    diaryDetailsState.postValue(
                        DiarySubmitForm(
                            true, DiaryResponse(
                                diaryResponse?.status,
                                diaryResponse?.code
                            ), it.diary.mainInfo
                        )
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                diaryDetailsState.postValue(DiarySubmitForm(false, DiaryResponse(e.message, 500)))
            }
        }
    }

    fun deleteSubmittedDiary(mainInfo: String?) {
        launch {
            deleteSubmittedDiaryFromDB(mainInfo)
        }
    }

    private suspend fun deleteSubmittedDiaryFromDB(mainInfo: String?) {
        withContext(Dispatchers.IO) {
            try {
                actualManager.deleteSubmittedDiary(mainInfo)
                diaryDetailsState.postValue(DeleteForm(true))
            } catch (e: Exception) {
                e.printStackTrace()
                diaryDetailsState.postValue(DeleteForm(false))
            }
        }
    }

    companion object {
        private const val DAY_7 = "Day 7"
    }
}
