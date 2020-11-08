package com.praeter.ui.base;

public interface BasePresenter<T extends BaseView> {

    void attachView(T view);

    void detachView();
}
