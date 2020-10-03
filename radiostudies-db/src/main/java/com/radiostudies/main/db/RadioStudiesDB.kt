package com.radiostudies.main.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.radiostudies.main.db.dao.*
import com.radiostudies.main.db.entity.*

/**
 * Created by eduardo.delito on 8/22/20.
 */
@Database(
    entities = [
        UserEntity::class,
        QuestionEntity::class,
        MainInfoEntity::class,
        ActualQuestionEntity::class,
        AreaEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class RadioStudiesDB : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun questionDao(): QuestionDao
    abstract fun mainInfoDao(): MainInfoDao
    abstract fun actualQuestionDao(): ActualQuestionDao
    abstract fun areaDao(): AreaDao

    companion object {
        const val DATABASE_NAME = "RadioStudiesDB"
    }
}
