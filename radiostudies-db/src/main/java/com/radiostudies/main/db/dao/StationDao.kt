package com.radiostudies.main.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.radiostudies.main.db.entity.StationEntity

/**
 * Created by eduardo.delito on 10/11/20.
 */
@Dao
interface StationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStation(StationEntity: List<StationEntity>)

    @Query("SELECT * FROM StationEntity WHERE place = :place")
    fun queryStation(place: String): StationEntity

    @Query("DELETE FROM StationEntity")
    fun deleteStation()
}
