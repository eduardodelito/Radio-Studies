package com.radiostudies.main.db.manager

import com.radiostudies.main.db.dao.*
import com.radiostudies.main.db.entity.*
import com.radiostudies.main.db.model.ActualQuestion
import com.radiostudies.main.model.Diaries
import com.radiostudies.main.model.Option

/**
 * Created by eduardo.delito on 10/4/20.
 */
interface ActualManager {
    fun insertArea(areas: List<AreaEntity>)

    fun insertStation(stations: List<StationEntity>)

    fun insertActualQuestion(actualQuestionEntity: List<ActualQuestionEntity>)

    fun queryQuestion(qID: Int): ActualQuestion

    fun queryAreas(): List<Option>

    fun saveDataQuestion(dataQuestionEntity: DataQuestionEntity)

    fun getSelectedArea(place: String): List<Option>

    fun saveCompletedActualQuestions(diaryEntity: DiaryEntity)

    fun updateDiary(diaryEntity: DiaryEntity)

    fun queryDataQuestions(): List<DataQuestionEntity>

    fun deleteSaveDataQuestions()

    fun getDiaryList(): List<DiaryEntity>

    fun getDiaries(mainInfo: String): List<Diaries>?

    fun queryDataQuestion(qCode: String?): DataQuestionEntity?

    fun validateMainInfo(panelNumber: String?, memberNumber: String?): Boolean

    fun loadMainInfo(panelNumber: String?): String

    fun isPanelNUmberExist(panelNumber: String?): Boolean

    fun queryDiary(panelNumber: String?): DiaryEntity?

    fun deleteSubmittedDiary(mainInfo: String?)
}

class ActualManagerImpl(
    private val areaDao: AreaDao,
    private val actualQuestionDao: ActualQuestionDao,
    private val dataQuestionDao: DataQuestionDao,
    private val stationDao: StationDao,
    private val diaryDao: DiaryDao
) : ActualManager {
    override fun insertArea(areas: List<AreaEntity>) {
        areaDao.deleteArea()
        areaDao.insertArea(areas)
    }

    override fun insertStation(stations: List<StationEntity>) {
        stationDao.deleteStation()
        stationDao.insertStation(stations)
    }

    override fun insertActualQuestion(actualQuestionEntity: List<ActualQuestionEntity>) {
        actualQuestionDao.deleteActualQuestion()
        actualQuestionDao.insertActualQuestion(actualQuestionEntity)
    }

    override fun queryQuestion(qID: Int) = actualQuestionDao.queryActualQuestion(qID)

    override fun queryAreas() = areaDao.queryArea()

    override fun saveDataQuestion(dataQuestionEntity: DataQuestionEntity) {
        dataQuestionDao.insertOrUpdate(dataQuestionEntity)
    }

    override fun getSelectedArea(place: String) = stationDao.queryStation(place).options

    override fun saveCompletedActualQuestions(diaryEntity: DiaryEntity) {
        diaryDao.insert(diaryEntity)
    }

    override fun updateDiary(diaryEntity: DiaryEntity) {
        diaryDao.updateDiary(diaryEntity.mainInfo, diaryEntity.diaries)
    }

    override fun queryDataQuestions() = dataQuestionDao.queryDataQuestions()

    override fun deleteSaveDataQuestions() {
        dataQuestionDao.deleteDataQuestion()
    }

    override fun getDiaryList(): List<DiaryEntity> = diaryDao.getDiaryList()

    override fun getDiaries(mainInfo: String) = diaryDao.getDiaries(mainInfo).diaries

    override fun queryDataQuestion(qCode: String?) = dataQuestionDao.queryDataQuestion(qCode)

    override fun validateMainInfo(panelNumber: String?, memberNumber: String?) =
        diaryDao.validateMainInfo(panelNumber, memberNumber)

    override fun loadMainInfo(panelNumber: String?) = diaryDao.loadMainInfo(panelNumber)

    override fun isPanelNUmberExist(panelNumber: String?) = diaryDao.panelNumberExist(panelNumber)

    override fun queryDiary(panelNumber: String?) =  diaryDao.getDiary(panelNumber)

    override fun deleteSubmittedDiary(mainInfo: String?) {
        diaryDao.deleteSubmittedDiary(mainInfo)
    }
}
