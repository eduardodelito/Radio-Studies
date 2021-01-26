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

    companion object {
        const val EMPTY_STRING = ""
    }
}
