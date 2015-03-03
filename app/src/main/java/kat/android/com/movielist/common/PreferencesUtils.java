package kat.android.com.movielist.common;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesUtils {

    private static PreferencesUtils sUtils;
    private SharedPreferences sharedPref;

    private static final String KEY_SHARED_PREF = "ANDROID_MOVIE_LIST";
    private static final int KEY_MODE_PRIVATE = 0;
    private static final String KEY_SESSION_USER_USERNAME = "sessionUserUsername";
    private static final String KEY_SESSION_USER_ID = "sessionUserID";
    private static final String KEY_SESSION_ID = "sessionID";

    public PreferencesUtils(Context context) {
        sharedPref = context.getSharedPreferences(KEY_SHARED_PREF,
                KEY_MODE_PRIVATE);
    }

    public static PreferencesUtils get(Context c) {
        if (sUtils == null) {
            sUtils = new PreferencesUtils(c.getApplicationContext());
        }
        return sUtils;
    }


    public void storeSessionUser(int userID, String userName, String session_ID) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(KEY_SESSION_USER_ID, userID);
        editor.putString(KEY_SESSION_USER_USERNAME, userName);
        editor.putString(KEY_SESSION_ID, session_ID);
        editor.apply();
    }

    public String getSessionUser() {
        return sharedPref.getString(KEY_SESSION_USER_USERNAME, null);
    }

    public boolean logoutSessionUser() {
        sharedPref.edit().putString(KEY_SESSION_USER_USERNAME, null).apply();
        return true;
    }

    public String getSessionID() {
        return sharedPref.getString(KEY_SESSION_ID, null);
    }

    public int getSessionUserID() {
        return sharedPref.getInt(KEY_SESSION_USER_ID, 0);
    }

}
