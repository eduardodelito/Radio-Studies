package com.radiostudies.main.db.manager

import com.radiostudies.main.db.dao.UserDao
import com.radiostudies.main.db.entity.UserEntity

/**
 * Created by eduardo.delito on 8/22/20.
 */
interface DBManager {
    fun insertUsers(users: List<UserEntity>)
    fun isUsernamePasswordValid(userName: String?, password: String?): Boolean
}

class DBManagerImpl(var userDao: UserDao) : DBManager {
    override fun insertUsers(users: List<UserEntity>) {
        userDao.deleteUsers()
        userDao.insertUsers(users)
    }

    override fun isUsernamePasswordValid(userName: String?, password: String?) =
        userDao.isUsenamePasswordValid(userName, password)
}
