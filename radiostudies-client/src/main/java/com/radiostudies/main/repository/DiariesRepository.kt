package com.radiostudies.main.repository

import com.radiostudies.main.client.RadioStudiesApiClient
import com.radiostudies.main.model.Diary
import com.radiostudies.main.model.DiaryResponse

interface DiariesRepository {
    suspend fun sendDiary(diary: Diary?): DiaryResponse?
}

class DiariesRepositoryImpl(private val client: RadioStudiesApiClient) : DiariesRepository {

    override suspend fun sendDiary(diary: Diary?) = client.getRadioStudiesResponse().sendDiary(diary)
}
