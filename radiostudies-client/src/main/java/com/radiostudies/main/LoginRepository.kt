package com.radiostudies.main

import com.radiostudies.main.client.RadioStudiesApiClient
import com.radiostudies.main.model.Login
import com.radiostudies.main.model.User

interface LoginRepository {
    suspend fun login(userName: String, password: String): User?
}

class LoginRepositoryImpl(private val client: RadioStudiesApiClient) : LoginRepository {
    override suspend fun login(userName: String, password: String) =
        client.getRadioStudiesResponse().login(Login(userName, password))
}
