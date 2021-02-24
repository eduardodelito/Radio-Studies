package com.radiostudies.main.repository

import com.radiostudies.main.client.RadioStudiesApiClient
import com.radiostudies.main.model.Diary

interface DiariesRepository {
    suspend fun sendDiaryList(diaries: List<Diary>): String?
}

class DiariesRepositoryImpl(private val client: RadioStudiesApiClient) : DiariesRepository {

    override suspend fun sendDiaryList(diaries: List<Diary>) = client.getRadioStudiesResponse().sendDiaries(diaries)
}
