package com.radiostudies.main.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.radiostudies.main.db.entity.ActualQuestionEntity
import com.radiostudies.main.db.model.ActualQuestion

/**
 * Created by eduardo.delito on 10/3/20.
 */
@Dao
interface ActualQuestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertActualQuestion(actualQuestionEntity: List<ActualQuestionEntity>)

    @Query("SELECT * FROM ActualQuestionEntity WHERE qId = :qId LIMIT 1")
    fun queryActualQuestion(qId: Int): ActualQuestion

    @Query("DELETE FROM ActualQuestionEntity")
    fun deleteActualQuestion()
}
