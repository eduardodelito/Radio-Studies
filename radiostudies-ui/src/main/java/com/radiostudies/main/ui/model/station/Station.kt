package com.radiostudies.main.ui.model.station

import com.radiostudies.main.db.entity.Option

/**
 * Created by eduardo.delito on 10/11/20.
 */
data class Station(val place: String, val options: List<Option>)