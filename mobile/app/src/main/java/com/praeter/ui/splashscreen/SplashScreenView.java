package com.praeter.ui.splashscreen;

import android.annotation.SuppressLint;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.praeter.R;
import com.praeter.navigator.Navigator;
import com.praeter.ui.base.BaseViewImpl;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
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

    interface ApiService {
        @GET("users/db")
        Single<String> getDbConnection();
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
                .fromAction(() -> {
                    makeCall();
                })
                .delay(5, TimeUnit.SECONDS)
                .doOnComplete(this::goToServicePickerActivity)
                .doOnError(Timber::e)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private void makeCall() {

        Timber.d("make call");

        Gson gson = new GsonBuilder()
                .disableHtmlEscaping()
                .setLenient()
                .create();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        logging.redactHeader("Authorization");
        logging.redactHeader("Cookie");

        OkHttpClient client =
                new OkHttpClient.Builder()
                        .addInterceptor(logging)
                        .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.48:3000/")
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        ApiService api = retrofit.create(ApiService.class);


        api.getDbConnection()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> Timber.d("result : %s", result),
                        Timber::e
                );

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
//            navigator.callSuccessfulSignUpActivity();
            context.finish();
        }
    }
}
