package com.radiostudies.main.ui.mapper

import com.radiostudies.main.db.entity.AreaEntity
import com.radiostudies.main.model.Option

/**
 * Created by eduardo.delito on 10/4/20.
 */
fun List<Option>.areaListModelToAreaListEntity() : List<AreaEntity> {
    return this.map {
        AreaEntity(
            id = 0,
            code = it.code,
            option = it.option
        )
    }
}

fun List<AreaEntity>.areaEntityListModelToAreaList() : List<Option> {
    return this.map {
        Option(
            code = it.code,
            option = it.option
        )
    }
}
