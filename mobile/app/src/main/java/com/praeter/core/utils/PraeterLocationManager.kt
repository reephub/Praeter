package com.praeter.core.utils

import android.location.Geocoder
import android.location.Location
import timber.log.Timber
import java.io.IOException

object PraeterLocationManager {
    fun getDeviceLocationToString(
        geocoder: Geocoder?,
        location: Location
    ): String {
        Timber.i("getDeviceLocationToString")
        var finalAddress = "" //This is the complete address.
        var finalCity = "" //This is the complete address.
        val latitude = location.latitude
        val longitude = location.longitude

        //get the address
        val builderAddr = StringBuilder()
        val builderCity = StringBuilder()
        try {
            val address = geocoder!!.getFromLocation(latitude, longitude, 1)
            Timber.e("addresses : %s", address)
            val maxLines = address[0].maxAddressLineIndex

            for (i in 0 until maxLines) {
                val addressStr = address[0].getAddressLine(i)
                Timber.d("addressStr : %s", addressStr)
                val cityStr = address[0].locality
                Timber.d("cityStr : %s", cityStr)
                builderAddr.append(addressStr)
                builderAddr.append(" ")
                builderCity.append(cityStr)
                builderCity.append(" ")
            }

            finalAddress = builderAddr.toString() //This is the complete address.
            finalCity = builderCity.toString() //This is the complete address.

            Timber.e("Address : $finalAddress | City : $finalCity") //This will display the final address.
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
        return finalAddress
    }

    fun convertLatLngLocationToString(location: Location): String {
        val lat = location.latitude.toString()
        val lng = location.longitude.toString()

        return StringBuilder().append(lat).append(",").append(lng).toString()
    }
}