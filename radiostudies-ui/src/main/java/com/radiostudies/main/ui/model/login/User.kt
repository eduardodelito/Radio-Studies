package com.radiostudies.main.ui.model.login

import java.util.*

/**
 * Created by eduardo.delito on 8/20/20.
 */
data class User(var id: Int, var userName: String?, var password: String?, var name: String?,
                var code: String?, var subCon: String?, var userType: String?, var status: Boolean,
                var area: String?, var createDate: Date?)
