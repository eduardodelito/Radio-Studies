package com.radiostudies.main.db.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.radiostudies.main.db.entity.ActualQuestionEntity

/**
 * Created by eduardo.delito on 10/3/20.
 */
interface ActualQuestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertActualQuestion(areaEntity: List<ActualQuestionEntity>)

    @Query("DELETE FROM ActualQuestionEntity")
    fun deleteActualQuestion()
}
