package com.radiostudies.main.client

import com.radiostudies.main.model.User
import retrofit2.http.POST

interface RadioStudiesApiService {
    @POST("api/Users")
    suspend fun login(): User
}
