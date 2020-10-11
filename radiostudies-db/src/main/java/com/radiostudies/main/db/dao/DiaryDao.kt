package com.radiostudies.main.db.dao

import androidx.room.Query
import com.radiostudies.main.db.entity.DiaryEntity
import com.radiostudies.main.db.model.Diary

/**
 * Created by eduardo.delito on 10/11/20.
 */
interface DiaryDao : BaseDao<DiaryEntity> {
    @Query("SELECT * FROM DiaryEntity WHERE panelNumber = :panelNumber LIMIT 1")
    fun queryDiary(panelNumber: String): Diary
}
