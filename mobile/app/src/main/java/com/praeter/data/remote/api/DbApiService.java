package com.praeter.data.remote.api;

import com.praeter.data.remote.dto.ApiResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;

public interface DbApiService {

    @GET("/db")
    Single<ApiResponse> getDbConnection();
}
