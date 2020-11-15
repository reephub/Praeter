package com.praeter.ui.splashscreen;

import android.annotation.SuppressLint;
import android.widget.TextView;

import com.praeter.R;
import com.praeter.navigator.Navigator;
import com.praeter.ui.base.BaseViewImpl;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;


@SuppressLint("NonConstantResourceId")
public class SplashScreenView extends BaseViewImpl<SplashScreenPresenter>
        implements SplashScreenContract.View {

    @BindView(R.id.tv_app_version)
    TextView tvAppVersion;

    @Inject
    Navigator navigator;

    private SplashScreenActivity context;


    @Inject
    SplashScreenView(SplashScreenActivity context) {
        this.context = context;
    }


    /////////////////////////////////
    //
    // OVERRIDE
    //
    /////////////////////////////////
    @Override
    public void onCreate() {

        getPresenter().attachView(this);

        ButterKnife.bind(this, context.findViewById(android.R.id.content));

        getPresenter().hasPermissions(context);

        getPresenter().getAppVersion();
    }

    @Override
    public void onDestroy() {
        getPresenter().detachView();
        context = null;
    }

    /////////////////////////////////
    //
    // PRESENTER
    //
    /////////////////////////////////
    @Override
    public void onPermissionsGranted() {

        Completable
                .complete()
                .delay(3, TimeUnit.SECONDS)
                .doOnComplete(this::goToServicePickerActivity)
                .doOnError(Timber::e)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    @Override
    public void onPermissionsDenied() {
        Timber.e("onPermissionsDenied()");
        closeApp();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void displayAppVersion(String appVersion) {
        tvAppVersion.setText(context.getString(R.string.version_placeholder) + appVersion);
    }

    @Override
    public void closeApp() {
        context.finish();
    }


    /////////////////////////////////
    //
    // CLASS METHODS
    //
    /////////////////////////////////

    private void goToServicePickerActivity() {
        Timber.i("goToServicePickerActivity()");
        if (context != null && navigator != null) {
            navigator.callServicePickerActivity();
//            navigator.callMainActivity();
            context.finish();
        }
    }
}
