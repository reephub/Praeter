package com.praeter.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;

import java.io.IOException;
import java.util.List;

import timber.log.Timber;

public class DeviceManager {

    private DeviceManager() {
    }

    public static void logDeviceInfo() {
        Timber.d("logDeviceInfo()");
        Timber.i("SERIAL: %s ", Build.SERIAL);
        Timber.i("MODEL: %s ", Build.MODEL);
        Timber.i("ID: %s ", Build.ID);
        Timber.i("Manufacture: %s ", Build.MANUFACTURER);
        Timber.i("brand: %s ", Build.BRAND);
        Timber.i("type: %s ", Build.TYPE);
        Timber.i("user: %s ", Build.USER);
        Timber.i("BASE: %s ", Build.VERSION_CODES.BASE);
        Timber.i("INCREMENTAL: %s ", Build.VERSION.INCREMENTAL);
        Timber.i("SDK : %s ", Build.VERSION.SDK);
        Timber.i("BOARD: %s ", Build.BOARD);
        Timber.i("BRAND: %s ", Build.BRAND);
        Timber.i("HOST: %s ", Build.HOST);
        Timber.i("FINGERPRINT: %s ", Build.FINGERPRINT);
        Timber.i("Version Code: %s ", Build.VERSION.RELEASE);
    }

    public static String getSerial() {
        return Build.SERIAL;
    }

    public static String getModel() {
        return Build.MODEL;
    }

    public static String getID() {
        return Build.ID;
    }

    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    public static String getBrand() {
        return Build.BRAND;
    }

    public static String getType() {
        return Build.TYPE;
    }

    public static String getUser() {
        return Build.USER;
    }

    public static int getVersionBase() {
        return Build.VERSION_CODES.BASE;
    }

    public static String getVersionIncremental() {
        return Build.VERSION.INCREMENTAL;
    }

    public static String getSdkVersion() {
        return Build.VERSION.SDK;
    }

    public static String getBoard() {
        return Build.BOARD;
    }

    public static String getHost() {
        return Build.HOST;
    }

    public static String getFingerPrint() {
        return Build.FINGERPRINT;
    }

    public static String getVersionCode() {
        return Build.VERSION.RELEASE;
    }


    public static String getDeviceLocationToString(final Geocoder geocoder, final Location location, final Context context) {

        Timber.i("--- Class Utils ---  getDeviceLocation() ");

        String finalAddress = ""; //This is the complete address.
        String finalCity = ""; //This is the complete address.

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        //get the address
        StringBuilder builderAddr = new StringBuilder();
        StringBuilder builderCity = new StringBuilder();

        try {
            List<Address> address = geocoder.getFromLocation(latitude, longitude, 1);
            Timber.e("addresses : %s", address);
            int maxLines = address.get(0).getMaxAddressLineIndex();
            for (int i = 0; i < maxLines; i++) {
                String addressStr = address.get(0).getAddressLine(i);
                Timber.d("addressStr : %s", addressStr);

                String cityStr = address.get(0).getLocality();
                Timber.d("cityStr : %s", cityStr);

                builderAddr.append(addressStr);
                builderAddr.append(" ");

                builderCity.append(cityStr);
                builderCity.append(" ");
            }

            finalAddress = builderAddr.toString(); //This is the complete address.
            finalCity = builderCity.toString(); //This is the complete address.

            Timber.tag("OHOH").e("Adresse : " + finalAddress + " | " + "City : " + finalCity); //This will display the final address.

        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }

        return finalAddress;
    }
}
