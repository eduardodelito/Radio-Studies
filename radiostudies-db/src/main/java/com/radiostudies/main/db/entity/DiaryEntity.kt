package com.radiostudies.main.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.radiostudies.main.db.model.DataQuestion

/**
 * Created by eduardo.delito on 10/11/20.
 */
@Entity(tableName = "DiaryEntity")
data class DiaryEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @ColumnInfo(name = "mainInfo") var mainInfo: String?,
    @ColumnInfo(name = "dataQuestions") var dataQuestions: List<DataQuestion>?,
    @ColumnInfo(name = "diaries") var diaries: List<Diaries>?
)

data class Diaries(
    val timeOfListening: List<Option>,
    val stations: List<Option>,
    val placeOfListening: List<Option>,
    val device: List<Option>
)
