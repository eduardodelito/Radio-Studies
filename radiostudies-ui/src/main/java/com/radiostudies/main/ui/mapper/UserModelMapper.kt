package com.radiostudies.main.ui.mapper

import com.radiostudies.main.db.entity.UserEntity
import com.radiostudies.main.model.User

fun User.userModelToUserEntity() : UserEntity {
    return UserEntity(
        id = 0,
        userID = UserID,
        firstName = FirstName,
        lastName = LastName,
        userName = UserName,
        password = Password,
        code = Code,
        subCon = SubCon,
        userType = UserType,
        status = Status,
        area = Area,
        createDate = CreatedDate
    )
}
