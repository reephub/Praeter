package com.praeter.data.pref

enum class SharedKeys(val sharedPreferenceKey: String) {

    VERSION_CODE_KEY("PREF_VERSION_CODE_KEY"),

    // Last Features key name
    LAST_FEATURES("PREF_LAST_FEATURES"),

    // access token key name
    PREF_KEY_ACCESS_TOKEN("PREF_KEY_ACCESS_TOKEN"),

    // user login status key name
    PREF_KEY_USER_LOGIN_STATUS("PREF_KEY_USER_LOGIN_STATUS")
}