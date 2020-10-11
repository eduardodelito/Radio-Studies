package com.radiostudies.main.db.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.radiostudies.main.db.entity.Option
import com.radiostudies.main.db.model.DataQuestion
import java.lang.reflect.Type

/**
 * Created by eduardo.delito on 10/5/20.
 */
class QuestionsConverter {
    @TypeConverter
    fun fromString(value: String?): List<String?>? {
        val listType: Type = object : TypeToken<List<String?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<String?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromOptionString(value: String?): List<Option?>? {
        val listType: Type = object : TypeToken<List<Option?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromOptionList(list: List<Option?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromDataQuestionsString(value: String?): List<DataQuestion?>? {
        val listType: Type = object : TypeToken<List<DataQuestion?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromDataQuestionList(list: List<DataQuestion?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
}
