package com.praeter.core.utils

import android.annotation.SuppressLint
import android.os.Build
import timber.log.Timber

@SuppressLint("HardwareIds")
object PraeterDeviceManager {
    fun logDeviceInfo() {
        Timber.d("logDeviceInfo()")
        Timber.i("SERIAL: %s ", Build.SERIAL)
        Timber.i("MODEL: %s ", Build.MODEL)
        Timber.i("ID: %s ", Build.ID)
        Timber.i("Manufacture: %s ", Build.MANUFACTURER)
        Timber.i("brand: %s ", Build.BRAND)
        Timber.i("type: %s ", Build.TYPE)
        Timber.i("user: %s ", Build.USER)
        Timber.i("BASE: %s ", Build.VERSION_CODES.BASE)
        Timber.i("INCREMENTAL: %s ", Build.VERSION.INCREMENTAL)
        Timber.i("SDK : %s ", Build.VERSION.SDK)
        Timber.i("BOARD: %s ", Build.BOARD)
        Timber.i("BRAND: %s ", Build.BRAND)
        Timber.i("HOST: %s ", Build.HOST)
        Timber.i("FINGERPRINT: %s ", Build.FINGERPRINT)
        Timber.i("Version Code: %s ", Build.VERSION.RELEASE)
    }

    val serial: String
        get() = Build.SERIAL
    val model: String
        get() = Build.MODEL
    val iD: String
        get() = Build.ID
    val manufacturer: String
        get() = Build.MANUFACTURER
    val brand: String
        get() = Build.BRAND
    val type: String
        get() = Build.TYPE
    val user: String
        get() = Build.USER
    val versionBase: Int
        get() = Build.VERSION_CODES.BASE
    val versionIncremental: String
        get() = Build.VERSION.INCREMENTAL
    val sdkVersion: String
        get() = Build.VERSION.SDK
    val board: String
        get() = Build.BOARD
    val host: String
        get() = Build.HOST
    val fingerPrint: String
        get() = Build.FINGERPRINT
    val versionCode: String
        get() = Build.VERSION.RELEASE
}