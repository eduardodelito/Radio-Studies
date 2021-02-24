package com.radiostudies.main.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.radiostudies.main.model.Diaries

/**
 * Created by eduardo.delito on 10/11/20.
 */
@Entity(tableName = "DiaryEntity")
data class DiaryEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @ColumnInfo(name = "userId") var userId: String?,
    @ColumnInfo(name = "panelNumber") var panelNumber: String?,
    @ColumnInfo(name = "memberNumber") var memberNumber: String?,
    @ColumnInfo(name = "mainInfo") var mainInfo: String?,
    @ColumnInfo(name = "dataQuestions") var dataQuestions: List<com.radiostudies.main.model.DataQuestion>?,
    @ColumnInfo(name = "diaries") var diaries: List<Diaries>?
)
