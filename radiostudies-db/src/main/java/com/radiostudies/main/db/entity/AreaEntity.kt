package com.radiostudies.main.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by eduardo.delito on 10/3/20.
 */
@Entity(tableName = "AreaEntity")
data class AreaEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @ColumnInfo(name = "code") var code: String?,
    @ColumnInfo(name = "value") var value: String?,
    @ColumnInfo(name = "otherValue") var otherValue: String?,
    @ColumnInfo(name = "isManualInput") var isManualInput: Boolean?
)
