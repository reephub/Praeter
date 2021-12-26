package com.praeter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.multidex.MultiDexApplication
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.praeter.core.utils.PraeterDeviceManager
import com.praeter.core.utils.PraeterPreferenceManager
import com.praeter.ui.login.LoginActivity
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class PraeterApplication : MultiDexApplication() {

    companion object {
        var context: Context? = null

        @SuppressLint("StaticFieldLeak")
        private var mInstance: PraeterApplication? = null

        @get:Synchronized
        val instance: PraeterApplication?
            get() {
                if (mInstance == null) {
                    mInstance = PraeterApplication()
                }
                return mInstance
            }
    }

    private var pref: PraeterPreferenceManager? = null

    override fun onCreate() {
        super.onCreate()

        //Timber stuff
        Timber.plant(Timber.DebugTree())
        Timber.d("onCreate()")

        context = this

        if (BuildConfig.DEBUG) {
            init()
        }

        Timber.d("Application successfully created")
    }

    private fun init() {
        Timber.d("init stuff")

        // Operations on FirebaseCrashlytics.
        val crashlytics: FirebaseCrashlytics = FirebaseCrashlytics.getInstance()
        crashlytics.setUserId("Wayne-ID") // Set a key to a string.
        crashlytics.setCustomKey("Device", PraeterDeviceManager.manufacturer)
        crashlytics.setCustomKey("Model", PraeterDeviceManager.model)
        crashlytics.setCustomKey("Location", "Why?")
        crashlytics.setCustomKey("int_key", 23)
        crashlytics.setCustomKey("boolean_key", false)

        //Stetho stuff
        //Stetho.initializeWithDefaults(this);
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        Timber.d("onnTrimMemoryLevel()")
    }

    val prefManager: PraeterPreferenceManager?
        get() {
            if (pref == null) {
                pref = PraeterPreferenceManager(context!!)
            }
            return pref
        }

    fun logout() {
        pref?.clear()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

}