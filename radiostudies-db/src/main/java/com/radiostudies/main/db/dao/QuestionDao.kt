package com.radiostudies.main.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.radiostudies.main.db.entity.QuestionEntity

/**
 * Created by eduardo.delito on 9/29/20.
 */
@Dao
interface QuestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertQuestion(questions: List<QuestionEntity>)

    @Query("UPDATE QuestionEntity SET answers=:answers WHERE question = :question")
    fun update(question: String?, answers: List<String>?)

    @Query("SELECT answers FROM QuestionEntity WHERE question = :question LIMIT 1")
    fun querySelectedIndex(question: String?): List<String>?

    @Query("DELETE FROM QuestionEntity")
    fun deleteQuestion()
}
