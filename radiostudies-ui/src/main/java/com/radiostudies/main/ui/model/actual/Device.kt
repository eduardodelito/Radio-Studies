package com.radiostudies.main.ui.model.actual

import com.radiostudies.main.db.model.ActualQuestion

/**
 * Created by eduardo.delito on 11/2/20.
 */
data class Device(val code: String?, val option: String?, val childQuestions: List<ActualQuestion>)
