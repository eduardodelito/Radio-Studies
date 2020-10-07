package com.radiostudies.main.db.manager

import com.radiostudies.main.db.dao.ActualQuestionDao
import com.radiostudies.main.db.dao.AreaDao
import com.radiostudies.main.db.entity.ActualQuestionEntity
import com.radiostudies.main.db.entity.AreaEntity
import com.radiostudies.main.db.model.ActualQuestion

/**
 * Created by eduardo.delito on 10/4/20.
 */
interface ActualManager {
    fun insertArea(areas: List<AreaEntity>)

    fun insertActualQuestion(actualQuestionEntity: List<ActualQuestionEntity>)

    fun queryQuestion(qID: Int): ActualQuestion?

    fun queryArea(): List<AreaEntity>?
}

class ActualManagerImpl(
    private val areaDao: AreaDao,
    private val actualQuestionDao: ActualQuestionDao
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

    override fun queryArea(): List<AreaEntity>? = areaDao.queryArea()
}