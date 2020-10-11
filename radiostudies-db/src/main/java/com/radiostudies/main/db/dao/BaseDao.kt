package com.radiostudies.main.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update


/**
 * Created by eduardo.delito on 10/11/20.
 */
@Dao
interface BaseDao<T> {
    @Insert
    fun insert(vararg entityToInsert: T)

    @Insert
    fun insertAll(entitiesToInsert: List<T>?)

    @Insert
    fun insertAll(entitiesToInsert: Array<T>?)

    @Update
    fun update(entityToInsert: T)

    @Update
    fun updateAll(entitiesToInsert: List<T>?)

    @Update
    fun updateAll(entitiesToInsert: Array<T>?)

    @Delete
    fun delete(entityToDelete: T)

    @Delete
    fun delete(entitiesToDelete: List<T>?)

    @Delete
    fun delete(entitiesToDelete: Array<T>?)
}
