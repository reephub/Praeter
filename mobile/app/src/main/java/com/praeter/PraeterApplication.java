package com.praeter;

import android.content.Context;
import android.content.Intent;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.praeter.ui.login.LoginActivity;
import com.praeter.utils.DeviceManager;
import com.praeter.utils.MyPreferenceManager;

import timber.log.Timber;

public class PraeterApplication extends MultiDexApplication {

    private static Context mContext;
    private static PraeterApplication mInstance;

    private MyPreferenceManager pref;

    public static synchronized PraeterApplication getInstance() {
        if (mInstance == null) {
            mInstance = new PraeterApplication();
        }
        return mInstance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //Timber stuff
        Timber.plant(new Timber.DebugTree());
        Timber.d("onCreate()");

        mContext = this;

        if (BuildConfig.DEBUG) {
            this.init();
        }

        Timber.d("Application successfully created");
    }

    private void init() {
        Timber.d("init stuff");

        // Operations on FirebaseCrashlytics.
        FirebaseCrashlytics crashlytics = FirebaseCrashlytics.getInstance();
        crashlytics.setUserId("Wayne-ID");// Set a key to a string.
        crashlytics.setCustomKey("Device", DeviceManager.getManufacturer());
        crashlytics.setCustomKey("Model", DeviceManager.getModel());
        crashlytics.setCustomKey("Location", "Why?");
        crashlytics.setCustomKey("int_key", 23);
        crashlytics.setCustomKey("boolean_key", false);

        //Stetho stuff
        //Stetho.initializeWithDefaults(this);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);

        Timber.d("onnTrimMemoryLevel()");
    }

    public static Context getContext() {
        return mContext;
    }


    public MyPreferenceManager getPrefManager() {
        if (pref == null) {
            pref = new MyPreferenceManager(getContext());
        }

        return pref;
    }


    public void logout() {
        pref.clear();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
