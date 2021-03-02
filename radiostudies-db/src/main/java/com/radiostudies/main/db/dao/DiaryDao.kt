package com.radiostudies.main.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.radiostudies.main.db.entity.DiaryEntity
import com.radiostudies.main.model.Diaries

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

    @Query("SELECT * from DiaryEntity WHERE panelNumber = :panelNumber AND memberNumber =:memberNumber LIMIT 1")
    fun validateMainInfo(panelNumber: String?, memberNumber: String?): Boolean

    @Query("SELECT mainInfo from DiaryEntity WHERE panelNumber = :panelNumber ORDER BY memberNumber DESC LIMIT 1")
    fun loadMainInfo(panelNumber: String?): String

    @Query("SELECT * from DiaryEntity WHERE panelNumber = :panelNumber LIMIT 1")
    fun panelNumberExist(panelNumber: String?): Boolean

    @Query("SELECT * from DiaryEntity WHERE panelNumber = :panelNumber LIMIT 1")
    fun getDiary(panelNumber: String?): DiaryEntity

    @Query("DELETE FROM DiaryEntity WHERE mainInfo = :mainInfo")
    fun deleteSubmittedDiary(mainInfo: String?)
}
