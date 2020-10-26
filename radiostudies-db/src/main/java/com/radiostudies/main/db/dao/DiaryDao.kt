package com.radiostudies.main.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.radiostudies.main.db.entity.Diaries
import com.radiostudies.main.db.entity.DiaryEntity

/**
 * Created by eduardo.delito on 10/11/20.
 */
@Dao
interface DiaryDao : BaseDao<DiaryEntity> {
    @Query("SELECT * from DiaryEntity ORDER BY id ASC")
    fun getDiaryList(): List<DiaryEntity>

    @Query("UPDATE DiaryEntity SET diaries=:diaries WHERE mainInfo = :mainInfo")
    fun updateDiary(mainInfo: String?, diaries: List<Diaries>?)

    @Query("SELECT * from DiaryEntity WHERE mainInfo = :mainInfo LIMIT 1")
    fun getDiaries(mainInfo: String?): DiaryEntity
}
