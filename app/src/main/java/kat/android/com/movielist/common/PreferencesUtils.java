package kat.android.com.movielist.common;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesUtils {

    private static PreferencesUtils sUtils;
    private SharedPreferences sharedPref;

    // *****************  preferences data *****************
    private static final String KEY_SHARED_PREF = "ANDROID_MOVIE_LIST";
    private static final int KEY_MODE_PRIVATE = 0;

    //************** session user data **************
    private static final String KEY_SESSION_USER_USERNAME = "sessionUserUsername";
    private static final String KEY_SESSION_USER_ID = "sessionUserID";
    private static final String KEY_SESSION_ID = "sessionID";
    private static final String KEY_USER_LOGIN_BOOLEAN_LABEL = "userBooleanLoginLabel";

    //************** guest data **************
    private static final String KEY_GUEST_SESSION_ID = "guestSessionID";
    private static final String KEY_GUEST_BOOLEAN_LABEL = "guestBooleanLabel";

    //************** discover data **************
    private static final String KEY_DISCOVER_ADULT = "discoverADULT";
    private static final String KEY_DISCOVER_PRIMARY_RELEASE_YEAR = "discoverRELEASE";
    private static final String KEY_DISCOVER_PRIMARY_RELEASE_YEAR_ORDER = "discoverRELEASE_ORDER";
    private static final String KEY_DISCOVER_SORT_BY = "discoverSORT";

    private static final String KEY_DISCOVER_VOTE_AVERAGE = "discoverVOTE_AVG";
    private static final String KEY_DISCOVER_VOTE_AVERAGE_ORDER = "discoverVOTE_ORDER";
    private static final String KEY_DISCOVER_PEOPLE = "discoverPEOPLE";

    //***********************************  PREFERENCES ********************************************************
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

    //***********************************  SESSION ********************************************************
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

    //************************************ DISCOVER ********************************************************

    public void setAdult(boolean adult) {
        sharedPref.edit().putBoolean(KEY_DISCOVER_ADULT, adult).apply();
    }

    public boolean isAdult() {
        return sharedPref.getBoolean(KEY_DISCOVER_ADULT, false);
    }

    public void setReleaseYear(String year) {
        sharedPref.edit().putString(KEY_DISCOVER_PRIMARY_RELEASE_YEAR, year).apply();
    }

    public String getReleaseYear() {
        return sharedPref.getString(KEY_DISCOVER_PRIMARY_RELEASE_YEAR, null);
    }

    public void setReleaseOrder(String releaseOrder) {
        sharedPref.edit().putString(KEY_DISCOVER_PRIMARY_RELEASE_YEAR_ORDER, releaseOrder).apply();
    }

    public String getReleaseOrder() {
        return sharedPref.getString(KEY_DISCOVER_PRIMARY_RELEASE_YEAR_ORDER, null);
    }

    public void setSortOrder(String sortOrder) {
        sharedPref.edit().putString(KEY_DISCOVER_SORT_BY, sortOrder).apply();
    }

    public String getSortOrder() {
        return sharedPref.getString(KEY_DISCOVER_SORT_BY, null);
    }

    public void setVoteAvg(String voteAvg) {
        sharedPref.edit().putString(KEY_DISCOVER_VOTE_AVERAGE, voteAvg).apply();
    }

    public String getVoteAvg() {
        return sharedPref.getString(KEY_DISCOVER_VOTE_AVERAGE, null);
    }

    public void setVoteOrder(String voteOrder) {
        sharedPref.edit().putString(KEY_DISCOVER_VOTE_AVERAGE_ORDER, voteOrder).apply();
    }

    public String getVoteOrder() {
        return sharedPref.getString(KEY_DISCOVER_VOTE_AVERAGE_ORDER, null);
    }

    //people pref , should be KeyMap

    public void resetDiscoverData() {
        sharedPref.edit().putBoolean(KEY_DISCOVER_ADULT, false)
                .putString(KEY_DISCOVER_PRIMARY_RELEASE_YEAR, null)
                .putString(KEY_DISCOVER_PRIMARY_RELEASE_YEAR_ORDER, null)
                .putString(KEY_DISCOVER_SORT_BY, null)
                .putString(KEY_DISCOVER_VOTE_AVERAGE, null)
                .putString(KEY_DISCOVER_VOTE_AVERAGE_ORDER, null)
                .apply();
    }

}
