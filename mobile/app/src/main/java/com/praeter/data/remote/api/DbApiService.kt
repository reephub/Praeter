package com.praeter.data.remote.api

import com.praeter.data.remote.dto.ApiResponse
import retrofit2.http.GET

interface DbApiService {
    @GET("/db")
    suspend fun dbConnection(): ApiResponse
}