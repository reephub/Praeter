package com.praeter.data.remote

import com.praeter.data.remote.api.DbApiService
import com.praeter.data.remote.api.GoogleDirectionsApiService
import com.praeter.data.remote.api.UserApiService
import com.praeter.data.remote.dto.ApiResponse
import com.praeter.data.remote.dto.directions.GoogleDirectionsResponse
import com.praeter.data.remote.dto.user.User
import javax.inject.Inject

class ApiImpl @Inject constructor(
    dbApiService: DbApiService,
    userApiService: UserApiService,
    googleDirectionsApiService: GoogleDirectionsApiService
) : IApi {

    private var mdbApiService: DbApiService = dbApiService
    private var mUserApiService: UserApiService = userApiService
    private var mGoogleDirectionsApiService: GoogleDirectionsApiService = googleDirectionsApiService


    override suspend fun dbConnection(): ApiResponse {
        return mdbApiService.dbConnection()
    }

    override suspend fun loginUser(user: User): ApiResponse {
        return mUserApiService.login(user)
    }

    override suspend fun saveUser(user: User): ApiResponse {
        return mUserApiService.saveUser(user)
    }

    override suspend fun getDirections(
        origin: String,
        destination: String
    ): GoogleDirectionsResponse {
        return mGoogleDirectionsApiService.getDirections(origin, destination)
    }
}