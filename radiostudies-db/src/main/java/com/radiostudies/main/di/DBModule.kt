package com.radiostudies.main.di

import android.app.Application
import androidx.room.Room
import com.radiostudies.main.db.RadioStudiesDB
import com.radiostudies.main.db.dao.UserDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by eduardo.delito on 8/22/20.
 */
@Module
class DBModule(private var application: Application) {
    private var radioStudiesDB: RadioStudiesDB

    init {
        synchronized(this) {
            val instance = Room.databaseBuilder(
                application,
                RadioStudiesDB::class.java,
                RadioStudiesDB.DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
            radioStudiesDB = instance
            instance
        }
    }

    @Singleton
    @Provides
    fun providesRoomDatabase(): RadioStudiesDB {
        return radioStudiesDB
    }

    @Singleton
    @Provides
    fun providesUserDao(radioStudiesDB: RadioStudiesDB): UserDao {
        return radioStudiesDB.userDao()
    }
}
