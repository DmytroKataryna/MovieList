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
    private static final String KEY_USER_LOGIN_BOOLEAN_LABEL = "userBooleanLoginLabel";

    private static final String KEY_GUEST_SESSION_ID = "guestSessionID";
    private static final String KEY_GUEST_BOOLEAN_LABEL = "guestBooleanLabel";

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
        setGuest(false);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(KEY_SESSION_USER_ID, userID);
        editor.putString(KEY_SESSION_USER_USERNAME, userName);
        editor.putString(KEY_SESSION_ID, session_ID);
        editor.putBoolean(KEY_GUEST_BOOLEAN_LABEL, false);
        editor.apply();
    }

    public void storeGuestSessionUser(String session_ID) {
        setGuest(true);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(KEY_GUEST_BOOLEAN_LABEL, true);
        editor.putString(KEY_GUEST_SESSION_ID, session_ID);
        editor.apply();
    }


    public String getSessionUser() {
        return sharedPref.getString(KEY_SESSION_USER_USERNAME, null);
    }

    public boolean logoutSessionUser() {
        sharedPref.edit().putString(KEY_SESSION_USER_USERNAME, null)
                .putInt(KEY_SESSION_USER_ID, -1)
                .putString(KEY_SESSION_ID, null)
                .putBoolean(KEY_USER_LOGIN_BOOLEAN_LABEL, false)
                .putBoolean(KEY_GUEST_BOOLEAN_LABEL, true).apply();
        return true;
    }

    public boolean logoutGuestSessionUser() {
        sharedPref.edit().putString(KEY_GUEST_SESSION_ID, null)
                .putBoolean(KEY_USER_LOGIN_BOOLEAN_LABEL, true)
                .putBoolean(KEY_GUEST_BOOLEAN_LABEL, false).apply();
        return true;
    }

    public String getSessionID() {
        return sharedPref.getString(KEY_SESSION_ID, null);
    }

    public int getSessionUserID() {
        return sharedPref.getInt(KEY_SESSION_USER_ID, 0);
    }

    public String getGuestSessionID() {
        return sharedPref.getString(KEY_GUEST_SESSION_ID, null);
    }

    public boolean isGuest() {
        return sharedPref.getBoolean(KEY_GUEST_BOOLEAN_LABEL, false);
    }

    public void setGuest(boolean guest) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(KEY_GUEST_BOOLEAN_LABEL, guest);
        editor.apply();
    }

    public boolean isUserLogin() {
        return sharedPref.getBoolean(KEY_USER_LOGIN_BOOLEAN_LABEL, false);
    }

    public void setUserLogin(boolean userLogin) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(KEY_USER_LOGIN_BOOLEAN_LABEL, userLogin);
        editor.apply();
    }


}
