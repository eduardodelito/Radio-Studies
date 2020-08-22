package com.radiostudies.main.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.radiostudies.main.db.entity.UserEntity

/**
 * Created by eduardo.delito on 8/22/20.
 */
@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsers(users: List<UserEntity>)

    @Query("SELECT * from UserEntity ORDER BY id ASC")
    fun getUsers() : LiveData<List<UserEntity>>

    @Query("DELETE FROM UserEntity")
    fun deleteUsers()
}
