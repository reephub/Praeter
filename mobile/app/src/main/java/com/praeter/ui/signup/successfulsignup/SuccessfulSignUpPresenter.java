package com.praeter.ui.signup.successfulsignup;

import com.praeter.data.remote.PraeterService;
import com.praeter.data.remote.dto.User;
import com.praeter.navigator.Navigator;
import com.praeter.ui.base.BasePresenterImpl;

import javax.inject.Inject;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public class SuccessfulSignUpPresenter extends BasePresenterImpl<SuccessfulSignUpView>
        implements SuccessfulSignUpContract.Presenter {


    @Inject
    SuccessfulSignUpActivity activity;

    @Inject
    Navigator navigator;

    @Inject
    PraeterService service;

    CompositeDisposable compositeDisposable;

    @Inject
    SuccessfulSignUpPresenter() {
        compositeDisposable = new CompositeDisposable();
    }


    @Override
    public void makeRestSaveCall(User user) {
        Disposable disposable =
                service.saveUser(user)
                        .subscribe(apiResponse -> {
                            getView().onUserSaveSuccessful();
                        }, throwable -> {
                            getView().onUserSaveError(throwable);
                        });

        compositeDisposable.add(disposable);
    }

    @Override
    public void goToMainActivity() {
        if (null != navigator)
            navigator.callMainActivity();
    }

    @Override
    public void detachView() {
        if (null != compositeDisposable)
            compositeDisposable.clear();
        super.detachView();
    }
}
