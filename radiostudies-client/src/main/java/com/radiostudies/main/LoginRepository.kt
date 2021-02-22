package com.radiostudies.main

import com.radiostudies.main.client.RadioStudiesApiClient
import com.radiostudies.main.model.User

interface LoginRepository {
    suspend fun login(): User
}

class LoginRepositoryImpl(private val client: RadioStudiesApiClient) : LoginRepository {
    override suspend fun login() = client.getRadioStudiesResponse().login()
}
