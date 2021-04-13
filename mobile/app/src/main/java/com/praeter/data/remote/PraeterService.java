package com.praeter.data.remote;

import com.praeter.data.remote.api.DbApiService;
import com.praeter.data.remote.api.UserApiService;
import com.praeter.data.remote.dto.ApiResponse;
import com.praeter.data.remote.dto.User;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

public class PraeterService {

    DbApiService dbApiService;
    UserApiService userApiService;

    @Inject
    public PraeterService(UserApiService userApiService, DbApiService dbApiService) {
        Timber.d("PraeterService()");
        this.dbApiService = dbApiService;
        this.userApiService = userApiService;
    }


    /////////////////////////////////////
    //
    // GET
    //
    /////////////////////////////////////
    public Single<ApiResponse> getDbConnection() {
        return dbApiService
                .getDbConnection()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /////////////////////////////////////
    //
    // POST
    //
    /////////////////////////////////////
    public Single<ApiResponse> loginUser(User user) {
        return userApiService
                .login(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ApiResponse> saveUser(User user) {
        Timber.e("saveUser()");
        return userApiService
                .saveUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
