package com.praeter.data.remote.api

import com.praeter.data.remote.dto.ApiResponse
import com.praeter.data.remote.dto.user.User
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST

interface UserApiService {
    @POST("/users")
    suspend fun saveUser(@Body user: User): ApiResponse

    @PATCH("/users/login")
    suspend fun login(@Body user: User): ApiResponse
}