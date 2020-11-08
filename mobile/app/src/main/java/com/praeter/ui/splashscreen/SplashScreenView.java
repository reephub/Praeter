package com.praeter.ui.splashscreen;

import android.annotation.SuppressLint;

import com.praeter.navigator.Navigator;
import com.praeter.ui.base.BaseViewImpl;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.ButterKnife;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;


@SuppressLint("NonConstantResourceId")
public class SplashScreenView extends BaseViewImpl<SplashScreenPresenter>
        implements SplashScreenContract.View {

    /*@BindView(R.id.ll_splash_content)
    RelativeLayout rlContent;

    @BindView(R.id.splash_video)
    VideoView splashVideoView;

    @BindView(R.id.tv_app_version)
    TextView tvAppVersion;*/

    @Inject
    Navigator navigator;

    private SplashScreenActivity context;

    private static final String ANDROID_RES_PATH = "android.resource://";
    private static final String SEPARATOR = "/";

    private int position = 0;

    private int shortAnimationDuration;


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

        // Retrieve and cache the system's default "short" animation time.
        shortAnimationDuration =
                context
                        .getResources()
                        .getInteger(android.R.integer.config_shortAnimTime);

        getPresenter().hasPermissions(context);
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
                .doOnComplete(this::goToLoginActivity)
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


    @Override
    public void closeApp() {
        context.finish();
    }


    /////////////////////////////////
    //
    // CLASS METHODS
    //
    /////////////////////////////////

    private void goToLoginActivity() {
        Timber.i("goToMainActivity()");
        if (context != null && navigator != null) {
            navigator.callLoginActivity();
            context.finish();
        }
    }
}
