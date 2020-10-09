package com.radiostudies.main.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.radiostudies.main.db.entity.AreaEntity
import com.radiostudies.main.db.entity.Option

/**
 * Created by eduardo.delito on 10/3/20.
 */
@Dao
interface AreaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArea(areaEntity: List<AreaEntity>)

    @Query("SELECT code, option from AreaEntity ORDER BY id ASC")
    fun queryArea(): List<Option>

    @Query("DELETE FROM AreaEntity")
    fun deleteArea()
}
