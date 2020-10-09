package com.radiostudies.main.db.manager

import com.radiostudies.main.db.dao.ActualQuestionDao
import com.radiostudies.main.db.dao.AreaDao
import com.radiostudies.main.db.dao.DataQuestionDao
import com.radiostudies.main.db.entity.ActualQuestionEntity
import com.radiostudies.main.db.entity.AreaEntity
import com.radiostudies.main.db.entity.DataQuestionEntity
import com.radiostudies.main.db.entity.Option
import com.radiostudies.main.db.model.ActualQuestion

/**
 * Created by eduardo.delito on 10/4/20.
 */
interface ActualManager {
    fun insertArea(areas: List<AreaEntity>)

    fun insertActualQuestion(actualQuestionEntity: List<ActualQuestionEntity>)

    fun queryQuestion(qID: Int): ActualQuestion

    fun queryAreas(): List<Option>

    fun saveDataQuestions(dataQuestionEntity: List<DataQuestionEntity>)

    fun saveDataQuestion(dataQuestionEntity: DataQuestionEntity)
}

class ActualManagerImpl(
    private val areaDao: AreaDao,
    private val actualQuestionDao: ActualQuestionDao,
    private val dataQuestionDao: DataQuestionDao
) : ActualManager {
    override fun insertArea(areas: List<AreaEntity>) {
        areaDao.deleteArea()
        areaDao.insertArea(areas)
    }

    override fun insertActualQuestion(actualQuestionEntity: List<ActualQuestionEntity>) {
        actualQuestionDao.deleteActualQuestion()
        actualQuestionDao.insertActualQuestion(actualQuestionEntity)
    }

    override fun queryQuestion(qID: Int) = actualQuestionDao.queryActualQuestion(qID)

    override fun queryAreas() = areaDao.queryArea()

    override fun saveDataQuestions(dataQuestionEntity: List<DataQuestionEntity>) {
        dataQuestionDao.insertDataQuestions(dataQuestionEntity)
    }

    override fun saveDataQuestion(dataQuestionEntity: DataQuestionEntity) {
        dataQuestionDao.insertDataQuestion(dataQuestionEntity)
    }
}
