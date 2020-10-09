package com.radiostudies.main.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.radiostudies.main.db.entity.DataQuestionEntity

/**
 * Created by eduardo.delito on 10/8/20.
 */
@Dao
interface DataQuestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDataQuestions(dataQuestionEntity: List<DataQuestionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDataQuestion(dataQuestionEntity: DataQuestionEntity)

    @Query("SELECT * FROM DataQuestionEntity WHERE code = :code LIMIT 1")
    fun queryDataQuestion(code: String): DataQuestionEntity

    @Query("DELETE FROM DataQuestionEntity")
    fun deleteDataQuestion()
}
