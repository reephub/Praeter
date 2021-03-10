package com.praeter.utils;

import com.praeter.core.utils.PraeterDeviceManager;

public class Constants {

    private static final String HTTP = "http://";
    private static final String IP_ADDRESS = "192.168.0.48";
    private static final String EMULATOR_IP_ADDRESS = "10.0.2.2";
    private static final String PORT = ":3000";
    private static final String SEPARATOR = "/";

    //REST client Base URL
    public static final String BASE_ENDPOINT_PRAETER_URL =
            HTTP +
                    (PraeterDeviceManager.getModel().trim().toLowerCase().contains("sdk")
                            ? EMULATOR_IP_ADDRESS
                            : IP_ADDRESS) +
                    PORT;
}
