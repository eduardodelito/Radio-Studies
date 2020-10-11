package com.radiostudies.main.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.radiostudies.main.db.entity.DataQuestionEntity
import com.radiostudies.main.db.entity.Option

/**
 * Created by eduardo.delito on 10/8/20.
 */
@Dao
interface DataQuestionDao: BaseDao<DataQuestionEntity> {

    @Query("SELECT * FROM DataQuestionEntity WHERE code = :code LIMIT 1")
    fun queryDataQuestion(code: String?): DataQuestionEntity?

    @Query("SELECT * from DataQuestionEntity ORDER BY id ASC")
    fun queryDataQuestions(): List<DataQuestionEntity>

    @Query("DELETE FROM DataQuestionEntity")
    fun deleteDataQuestion()

    @Query("UPDATE DataQuestionEntity SET options=:options WHERE code = :code")
    fun update(code: String?, options: List<Option>?)

    @Transaction
    fun insertOrUpdate(dataQuestionEntity: DataQuestionEntity) {
        val dq = queryDataQuestion(dataQuestionEntity.code)
        if (dq?.code == null) {
            insert(dataQuestionEntity)
        } else {
            update(dq.code, dataQuestionEntity.options)
        }
    }
}
