package com.radiostudies.main.db.manager

import com.radiostudies.main.db.dao.MainInfoDao
import com.radiostudies.main.db.entity.MainInfoEntity

/**
 * Created by eduardo.delito on 10/1/20.
 */
interface MainInfoManager {
    fun insertMainInfo(mainInfoEntity: MainInfoEntity)
}

class MainInfoManagerImpl(private val mainInfoDao: MainInfoDao): MainInfoManager {
    override fun insertMainInfo(mainInfoEntity: MainInfoEntity) {
        mainInfoDao.deleteMainInfo()
        mainInfoDao.insertMainInfo(mainInfoEntity)
    }
}
