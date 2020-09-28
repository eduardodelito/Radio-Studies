package com.radiostudies.main.common.util

import android.content.Context
import java.io.IOException

/**
 * Created by eduardo.delito on 9/18/20.
 */

fun getJsonDataFromAsset(context: Context, fileName: String): String? {
    val jsonString: String
    try {
        jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
    } catch (ioException: IOException) {
        ioException.printStackTrace()
        return null
    }
    return jsonString
}
