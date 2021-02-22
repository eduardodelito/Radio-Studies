package com.radiostudies.main.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("UserID")
    val UserID: String?,
    @SerializedName("FirstName")
    val FirstName: String?,
    @SerializedName("LastName")
    val LastName: String?,
    @SerializedName("UserName")
    val UserName: String?,
    @SerializedName("Password")
    val Password: String?,
    @SerializedName("Code")
    val Code: String?,
    @SerializedName("SubCon")
    val SubCon: String?,
    @SerializedName("UserType")
    val UserType: String?,
    @SerializedName("Status")
    val Status: String?,
    @SerializedName("Area")
    val Area: String?,
    @SerializedName("CreatedDate")
    val CreatedDate: String?
)

data class Login(val userName: String?, val password: String?)
