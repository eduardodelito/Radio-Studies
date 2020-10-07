package com.radiostudies.main.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.radiostudies.main.db.entity.AreaEntity

/**
 * Created by eduardo.delito on 10/3/20.
 */
@Dao
interface AreaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArea(areaEntity: List<AreaEntity>)

    @Query("SELECT * from AreaEntity ORDER BY id ASC")
    fun queryArea(): List<AreaEntity>?

    @Query("DELETE FROM AreaEntity")
    fun deleteArea()
}
