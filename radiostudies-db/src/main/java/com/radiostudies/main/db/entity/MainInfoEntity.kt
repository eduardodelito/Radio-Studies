package com.radiostudies.main.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by eduardo.delito on 10/1/20.
 */
@Entity(tableName = "MainInfoEntity")
data class MainInfoEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @ColumnInfo(name = "panelNumber") var panelNumber: String?,
    @ColumnInfo(name = "memberNumber") var memberNumber: String?,
    @ColumnInfo(name = "municipality") var municipality: String?,
    @ColumnInfo(name = "barangay") var barangay: String? = null,
    @ColumnInfo(name = "nameOfRespondent") var nameOfRespondent: String?,
    @ColumnInfo(name = "address") var address: String?,
    @ColumnInfo(name = "age") var age: String?,
    @ColumnInfo(name = "gender") var gender: String?,
    @ColumnInfo(name = "dateOfInterview") var dateOfInterview: String?,
    @ColumnInfo(name = "timeStart") var timeStart: String?,
    @ColumnInfo(name = "timeEnd") var timeEnd: String?,
    @ColumnInfo(name = "dayOfWeek") var dayOfWeek: String?,
    @ColumnInfo(name = "contactNumber") var contactNumber: String?,
    @ColumnInfo(name = "ecoClass") var ecoClass: String?
)
