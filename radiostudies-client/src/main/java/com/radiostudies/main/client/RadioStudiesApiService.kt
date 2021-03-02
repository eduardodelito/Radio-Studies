package com.radiostudies.main.client

import com.radiostudies.main.model.Diary
import com.radiostudies.main.model.DiaryResponse
import com.radiostudies.main.model.Login
import com.radiostudies.main.model.User
import retrofit2.http.Body
import retrofit2.http.POST

interface RadioStudiesApiService {
    @POST("api/Users")
    suspend fun login(@Body body: Login): User?

    @POST("api/Panels")
    suspend fun sendDiary(@Body diaryList: List<Diary?>): DiaryResponse?
}
