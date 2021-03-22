package com.radiostudies.main.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.radiostudies.main.db.entity.DataQuestionEntity
import com.radiostudies.main.model.Option

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

    @Query("DELETE FROM DataQuestionEntity WHERE code = :qCode")
    fun deleteByQCode(qCode: String)

    @Transaction
    fun insertOrUpdate(dataQuestionEntity: DataQuestionEntity) {
        val dq = queryDataQuestion(dataQuestionEntity.code)
        if (dq?.code == null) {
            insert(dataQuestionEntity)
        } else {
            if (dq.code == Q2) {
                deleteByQCode(Q2a)
            } else if (dq.code == Q5) {
                deleteByQCode(Q5a)
                deleteByQCode(Q5b)
                deleteByQCode(Q5c)
                deleteByQCode(Q5d)
                deleteByQCode(Q5e)
                deleteByQCode(Q5f)
                deleteByQCode(Q5g)
                deleteByQCode(Q5h)
                deleteByQCode(Q5i)
                deleteByQCode(Q5j)
                deleteByQCode(Q5k)
            }
            update(dq.code, dataQuestionEntity.options)
        }
    }

    companion object {
        private const val Q2 = "Q2"
        private const val Q2a = "Q2a"
        private const val Q5 = "Q5"
        private const val Q5a = "Q5a"
        private const val Q5b = "Q5b"
        private const val Q5c = "Q5c"
        private const val Q5d = "Q5d"
        private const val Q5e = "Q5e"
        private const val Q5f = "Q5f"
        private const val Q5g = "Q5g"
        private const val Q5h = "Q5h"
        private const val Q5i = "Q5i"
        private const val Q5j = "Q5j"
        private const val Q5k = "Q5k"
    }
}
