package com.radiostudies.main.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.radiostudies.main.db.entity.DiaryEntity

/**
 * Created by eduardo.delito on 10/11/20.
 */
@Dao
interface DiaryDao : BaseDao<DiaryEntity> {
    @Query("SELECT * from DiaryEntity ORDER BY id ASC")
    fun getDiaryList() : List<DiaryEntity>
}
