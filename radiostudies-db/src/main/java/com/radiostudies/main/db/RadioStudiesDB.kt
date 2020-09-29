package com.radiostudies.main.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.radiostudies.main.db.dao.QuestionDao
import com.radiostudies.main.db.dao.UserDao
import com.radiostudies.main.db.entity.QuestionEntity
import com.radiostudies.main.db.entity.UserEntity

/**
 * Created by eduardo.delito on 8/22/20.
 */
@Database(
    entities = [UserEntity::class, QuestionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class RadioStudiesDB : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun questionDao(): QuestionDao
    companion object {
        const val DATABASE_NAME = "RadioStudiesDB"
    }
}
