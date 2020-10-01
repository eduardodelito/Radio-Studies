package com.radiostudies.main.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.radiostudies.main.db.entity.MainInfoEntity

/**
 * Created by eduardo.delito on 10/1/20.
 */
@Dao
interface MainInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMainInfo(mainInfoEntity: MainInfoEntity)

    @Query("DELETE FROM MainInfoEntity")
    fun deleteMainInfo()
}
