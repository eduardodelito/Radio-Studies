package com.radiostudies.main.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by eduardo.delito on 10/11/20.
 */
@Entity(tableName = "StationEntity")
data class StationEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @ColumnInfo(name = "place") var place: String,
    @ColumnInfo(name = "options") var options: List<Option>
)
