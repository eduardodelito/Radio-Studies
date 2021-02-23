package com.radiostudies.main.client

import com.radiostudies.main.model.Login
import com.radiostudies.main.model.User
import retrofit2.http.Body
import retrofit2.http.POST

interface RadioStudiesApiService {
    @POST("Users")
    suspend fun login(@Body body: Login): User?
}
