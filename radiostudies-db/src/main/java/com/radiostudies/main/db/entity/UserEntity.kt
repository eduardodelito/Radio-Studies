package com.radiostudies.main.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by eduardo.delito on 8/22/20.
 */
@Entity(tableName = "UserEntity")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @ColumnInfo(name = "userID") var userID: String?,
    @ColumnInfo(name = "firstName") var firstName: String?,
    @ColumnInfo(name = "lastName") var lastName: String?,
    @ColumnInfo(name = "userName") var userName: String?,
    @ColumnInfo(name = "password") var password: String?,
    @ColumnInfo(name = "code") var code: String?,
    @ColumnInfo(name = "subCon") var subCon: String?,
    @ColumnInfo(name = "userType") var userType: String?,
    @ColumnInfo(name = "status") var status: Boolean?,
    @ColumnInfo(name = "area") var area: String?,
    @ColumnInfo(name = "createDate") var createDate: String?
)
