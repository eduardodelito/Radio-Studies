package com.radiostudies.main.ui.mapper

import com.radiostudies.main.db.entity.AreaEntity
import com.radiostudies.main.db.model.Area

/**
 * Created by eduardo.delito on 10/4/20.
 */
fun List<Area>.areaListModelToAreaListEntity() : List<AreaEntity> {
    return this.map {
        AreaEntity(
            id = 0,
            code = it.code,
            place = it.place,
            isManualInput = it.isManualInput
        )
    }
}

fun List<AreaEntity>.areaEntityListModelToAreaList() : List<Area> {
    return this.map {
        Area(
            code = it.code,
            place = it.place,
            isManualInput = it.isManualInput
        )
    }
}
