package com.radiostudies.main.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.radiostudies.main.db.converter.QuestionsConverter
import com.radiostudies.main.db.dao.*
import com.radiostudies.main.db.entity.*

/**
 * Created by eduardo.delito on 8/22/20.
 */
@Database(
    entities = [
        UserEntity::class,
        QuestionEntity::class,
        ActualQuestionEntity::class,
        AreaEntity::class,
        StationEntity::class,
        DataQuestionEntity::class,
        DiaryEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(QuestionsConverter::class)
abstract class RadioStudiesDB : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun questionDao(): QuestionDao
    abstract fun actualQuestionDao(): ActualQuestionDao
    abstract fun areaDao(): AreaDao
    abstract fun dataQuestionDao(): DataQuestionDao
    abstract fun stationDao(): StationDao
    abstract fun diaryDao(): DiaryDao

    companion object {
        const val DATABASE_NAME = "RadioStudiesDB"
    }
}
