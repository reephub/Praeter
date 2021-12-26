package com.praeter.core.utils

import android.content.Context
import android.content.SharedPreferences

class PraeterPreferenceManager(  // Context
    var mContext: Context
) {
    // Shared Preferences
    var pref: SharedPreferences

    // Editor for Shared preferences
    var editor: SharedPreferences.Editor

    // Shared pref mode
    var PRIVATE_MODE = 0
    val isUserAlreadyLogged: Boolean
        get() = !pref.getString(KEY_USER_EMAIL, "")!!.isEmpty()

    /*
    public void storeUser(User user) {
        editor.putString(KEY_USER_ID, user.getId());
        editor.putString(KEY_USER_NAME, user.getName());
        editor.putString(KEY_USER_EMAIL, user.getEmail());
        editor.commit();

        Timber.e("User is stored in shared preferences. " + user.getName() + ", " + user.getEmail());
    }

    public User getUser() {
        if (pref.getString(KEY_USER_ID, null) != null) {
            String id, name, email;
            id = pref.getString(KEY_USER_ID, null);
            name = pref.getString(KEY_USER_NAME, null);
            email = pref.getString(KEY_USER_EMAIL, null);

            User user = new User(id, name, email);
            return user;
        }
        return null;
    }*/
    fun addNotification(notification: String) {

        // get old notifications
        var oldNotifications = notifications
        if (oldNotifications != null) {
            oldNotifications += "|$notification"
        } else {
            oldNotifications = notification
        }
        editor.putString(KEY_NOTIFICATIONS, oldNotifications)
        editor.commit()
    }

    val notifications: String?
        get() = pref.getString(KEY_NOTIFICATIONS, null)

    fun clear() {
        editor.clear()
        editor.commit()
    }

    companion object {
        // Sharedpref file name
        private const val PREF_NAME = "realtime_chat"

        // All Shared Preferences Keys
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_NOTIFICATIONS = "notifications"
    }

    // Constructor
    init {
        pref = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        //pref = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit()
    }
}