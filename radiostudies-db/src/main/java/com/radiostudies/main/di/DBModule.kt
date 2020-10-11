package com.radiostudies.main.di

import android.app.Application
import androidx.room.Room
import com.radiostudies.main.db.RadioStudiesDB
import com.radiostudies.main.db.dao.*
import com.radiostudies.main.db.manager.*
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

    @Singleton
    @Provides
    fun providesDBManager(userDao: UserDao): DBManager = DBManagerImpl(userDao)

    @Singleton
    @Provides
    fun providesQuestionDao(radioStudiesDB: RadioStudiesDB): QuestionDao =
        radioStudiesDB.questionDao()

    @Singleton
    @Provides
    fun providesQuestionManager(questionDao: QuestionDao): QuestionManager =
        QuestionManagerImpl(questionDao)

    @Singleton
    @Provides
    fun providesActualQuestion(radioStudiesDB: RadioStudiesDB): ActualQuestionDao =
        radioStudiesDB.actualQuestionDao()

    @Singleton
    @Provides
    fun providesAreaDao(radioStudiesDB: RadioStudiesDB): AreaDao =
        radioStudiesDB.areaDao()

    @Singleton
    @Provides
    fun providesDataQuestionDao(radioStudiesDB: RadioStudiesDB): DataQuestionDao =
        radioStudiesDB.dataQuestionDao()

    @Singleton
    @Provides
    fun providesStationDao(radioStudiesDB: RadioStudiesDB): StationDao = radioStudiesDB.stationDao()

    @Singleton
    @Provides
    fun providesDiaryDao(radioStudiesDB: RadioStudiesDB): DiaryDao = radioStudiesDB.diaryDao()

    @Singleton
    @Provides
    fun providesActualManager(
        areaDao: AreaDao,
        actualQuestionDao: ActualQuestionDao,
        dataQuestionDao: DataQuestionDao,
        stationDao: StationDao,
        diaryDao: DiaryDao
    ): ActualManager =
        ActualManagerImpl(areaDao, actualQuestionDao, dataQuestionDao, stationDao, diaryDao)
}
