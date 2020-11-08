package com.praeter.ui.login;

import com.praeter.ui.base.BasePresenterImpl;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

public class LoginPresenter extends BasePresenterImpl<LoginView>
        implements LoginActivityContract.Presenter {


    @Inject
    LoginActivity activity;

    @Inject
    LoginPresenter() {
    }

    @Override
    public void makeCallLogin() {

        getView().showLoading();

        // TODO : Make real REST api call

        Completable
                .complete()
                .delay(3, TimeUnit.SECONDS)
                .doOnComplete(() -> {

                    getView().hideLoading();

                    getView().onLoginSuccessful();

                })
                .doOnError(throwable -> {

                    getView().hideLoading();

                    Timber.e(throwable);

                    getView().onLoginFailed();
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

    }
}
