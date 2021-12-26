package com.praeter.data.pref

interface IPreferences {

    fun checkFirstRun()

    fun isFirstRun(): Boolean

    // last features activation
    fun setLastFeaturesActivation(isActivated: Boolean)
    fun getLastFeaturesActivation(): Boolean

}