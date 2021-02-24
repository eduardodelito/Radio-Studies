package com.radiostudies.main.common.manager

import android.content.SharedPreferences

interface SharedPreferencesManager {
    /**
     * Method for the last search key.
     * Saving/retrieving string key
     * @param key
     * @param value
     */
    fun savePrefs(key: String?, value: Boolean? = null): Boolean

    fun saveUserId(key: String?, value: String? = null): String
}

class SharedPreferencesManagerImpl(private val sharedPreferences: SharedPreferences): SharedPreferencesManager {

    /**
     * Method for the last search key.
     * Saving/retrieving string key
     * @param key
     * @param value
     */
    override fun savePrefs(key: String?, value: Boolean?): Boolean {
        value?.run {
            with(sharedPreferences.edit()) {
                putBoolean(key, value)
                apply()
            }
        }
        return sharedPreferences.getBoolean(key, false)
    }

    override fun saveUserId(key: String?, value: String?): String {
        value?.run {
            with(sharedPreferences.edit()) {
                putString(key, value)
                apply()
            }
        }
        return sharedPreferences.getString(key, EMPTY_STRING) ?: EMPTY_STRING
    }

    companion object {
        const val EMPTY_STRING = ""
    }
}
