package com.radiostudies.main.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.radiostudies.main.model.Option

/**
 * Created by eduardo.delito on 10/3/20.
 */
@Entity(tableName = "ActualQuestionEntity")
data class ActualQuestionEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @ColumnInfo(name = "qId") var qId: Int?,
    @ColumnInfo(name = "code") var code: String?,
    @ColumnInfo(name = "header") var header: String?,
    @ColumnInfo(name = "question") var question: String?,
    @ColumnInfo(name = "type") var type: String?,
    @ColumnInfo(name = "options") var options: List<Option>,
    @ColumnInfo(name = "isManualInput") var isManualInput: Boolean?
)
