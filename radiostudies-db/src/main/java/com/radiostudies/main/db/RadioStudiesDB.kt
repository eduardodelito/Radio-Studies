package com.radiostudies.main.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.radiostudies.main.db.dao.MainInfoDao
import com.radiostudies.main.db.dao.QuestionDao
import com.radiostudies.main.db.dao.UserDao
import com.radiostudies.main.db.entity.MainInfoEntity
import com.radiostudies.main.db.entity.QuestionEntity
import com.radiostudies.main.db.entity.UserEntity

/**
 * Created by eduardo.delito on 8/22/20.
 */
@Database(
    entities = [UserEntity::class, QuestionEntity::class, MainInfoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class RadioStudiesDB : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun questionDao(): QuestionDao
    abstract fun mainInfoDao(): MainInfoDao
    companion object {
        const val DATABASE_NAME = "RadioStudiesDB"
    }
}
