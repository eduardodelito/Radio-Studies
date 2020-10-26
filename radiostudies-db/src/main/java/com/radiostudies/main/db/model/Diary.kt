package com.radiostudies.main.db.model

import com.radiostudies.main.db.entity.Diaries
import java.io.Serializable

/**
 * Created by eduardo.delito on 10/11/20.
 */
data class Diary(
    var mainInfo: String?,
    var dataQuestions: List<DataQuestion>?,
    var diaries: List<Diaries>?
): Serializable
