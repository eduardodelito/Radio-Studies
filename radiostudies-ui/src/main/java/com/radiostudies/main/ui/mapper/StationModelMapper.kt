package com.radiostudies.main.ui.mapper

import com.radiostudies.main.db.entity.StationEntity
import com.radiostudies.main.ui.model.station.Station

/**
 * Created by eduardo.delito on 10/11/20.
 */

fun List<Station>.optionListModelToStationEntity() : List<StationEntity> {
    return this.map {
        StationEntity(
            id = 0,
            place = it.place,
            options = it.options
        )
    }
}
