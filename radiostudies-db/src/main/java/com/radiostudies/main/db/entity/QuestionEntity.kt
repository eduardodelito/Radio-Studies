package com.radiostudies.main.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by eduardo.delito on 9/29/20.
 */
@Entity(tableName = "QuestionEntity")
data class QuestionEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @ColumnInfo(name = "question") var question: String?,
    @ColumnInfo(name = "answers") var answers: String?
)