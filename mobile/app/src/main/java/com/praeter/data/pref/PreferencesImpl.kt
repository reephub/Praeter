package com.praeter.data.pref

import android.content.Context
import android.content.SharedPreferences
import com.praeter.BuildConfig


class PreferencesImpl(
    private val context: Context,
    private val prefFileName: String
) : IPreferences {

    // access token key name
    private var mPrefs: SharedPreferences? = null
    private val DOESNT_EXIST = -1

    init {
        mPrefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE)
    }

    override fun checkFirstRun() {

        // Get current version code
        val currentVersionCode: Int = BuildConfig.VERSION_CODE

        // Get saved version code
        val savedVersionCode =
            mPrefs?.getInt(SharedKeys.VERSION_CODE_KEY.sharedPreferenceKey, DOESNT_EXIST)

        // Check for first run or upgrade
        if (currentVersionCode == savedVersionCode) {
            // This is just a normal run
            return

        } else if (savedVersionCode == DOESNT_EXIST) {
            // TODO This is a new install (or the user cleared the shared preferences)

        } else if (currentVersionCode > savedVersionCode!!) {
            // TODO This is an upgrade
        }

        // Update the shared preferences with the current version code
        mPrefs?.edit()
            ?.putInt(SharedKeys.VERSION_CODE_KEY.sharedPreferenceKey, currentVersionCode)
            ?.apply()
    }

    override fun isFirstRun(): Boolean {
        var isFirstRun = false

        // Get current version code
        val currentVersionCode: Int = BuildConfig.VERSION_CODE

        // Get saved version code
        val savedVersionCode =
            mPrefs?.getInt(SharedKeys.VERSION_CODE_KEY.sharedPreferenceKey, DOESNT_EXIST)

        // Check for first run or upgrade
        if (currentVersionCode == savedVersionCode) {
            // This is just a normal run
            isFirstRun = false

        } else if (savedVersionCode == DOESNT_EXIST) {
            // TODO This is a new install (or the user cleared the shared preferences)
            isFirstRun = true

        } else if (currentVersionCode > savedVersionCode!!) {
            // TODO This is an upgrade
            isFirstRun = false
        }

        // Update the shared preferences with the current version code
        mPrefs?.edit()
            ?.putInt(SharedKeys.VERSION_CODE_KEY.sharedPreferenceKey, currentVersionCode)
            ?.apply()

        return isFirstRun
    }

    override fun setLastFeaturesActivation(isActivated: Boolean) {
        mPrefs?.edit()
            ?.putBoolean(SharedKeys.LAST_FEATURES.toString(), isActivated)
            ?.apply()
    }

    override fun getLastFeaturesActivation(): Boolean {
        return mPrefs?.getBoolean(SharedKeys.LAST_FEATURES.toString(), true)!!
    }
}