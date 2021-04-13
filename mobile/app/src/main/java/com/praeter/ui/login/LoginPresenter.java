package com.praeter.ui.login;

import com.praeter.data.remote.PraeterService;
import com.praeter.data.remote.dto.User;
import com.praeter.ui.base.BasePresenterImpl;

import javax.inject.Inject;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import timber.log.Timber;

public class LoginPresenter extends BasePresenterImpl<LoginView>
        implements LoginActivityContract.Presenter {


    @Inject
    LoginActivity activity;

    @Inject
    PraeterService service;

    CompositeDisposable compositeDisposable;

    @Inject
    LoginPresenter() {
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void makeCallLogin(User user) {

        getView().showLoading();

        // TODO : Make real REST api call

        /*Disposable dbDisposable =
                service.getDbConnection()
                        .subscribe(
                                apiResponse -> {
                                    Timber.e("db message : %s", apiResponse.toString());
                                },
                                throwable -> {
                                    getView().hideLoading();
                                    Timber.e(throwable);
                                });

        compositeDisposable.add(dbDisposable);*/

        Disposable loginDisposable =
                service.loginUser(user)
                        .subscribe(
                                apiResponse -> {
                                    Timber.e("login message : %s", apiResponse.toString());

                                    // TODO : save login in shared
                                    getView().onLoginSuccessful();
                                },
                                throwable -> {
                                    getView().hideLoading();
                                    Timber.e(throwable);
                                    getView().onLoginFailed();
                                });

        compositeDisposable.add(loginDisposable);
        /*Completable
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
*/
    }

    @Override
    public void detachView() {
        if (null != compositeDisposable)
            compositeDisposable.clear();

        super.detachView();
    }
}
