package com.praeter.data.remote.api;

import com.praeter.data.remote.dto.ApiResponse;
import com.praeter.data.remote.dto.User;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.PATCH;
import retrofit2.http.POST;

public interface UserApiService {

    @POST("/users")
    Single<ApiResponse> saveUser(@Body User user);

    @PATCH("/users/login")
    Single<ApiResponse> login(@Body User user);
}
