package com.praeter.data.remote

import com.praeter.data.remote.dto.ApiResponse
import com.praeter.data.remote.dto.directions.GoogleDirectionsResponse
import com.praeter.data.remote.dto.user.User

interface IApi {

    /////////////////////////////////////
    //
    // GET
    //
    /////////////////////////////////////
    suspend fun dbConnection(): ApiResponse

    /////////////////////////////////////
    //
    // POST
    //
    /////////////////////////////////////
    suspend fun loginUser(user: User): ApiResponse

    suspend fun saveUser(user: User): ApiResponse

    suspend fun getDirections(
        origin: String,
        destination: String
    ): GoogleDirectionsResponse

}