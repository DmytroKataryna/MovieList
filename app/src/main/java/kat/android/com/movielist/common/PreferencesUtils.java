package kat.android.com.movielist.common;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

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
    private static final String KEY_DISCOVER_PRIMARY_RELEASE_YEAR_POSITION = "discoverRELEASE_POS";

    private static final String KEY_DISCOVER_PRIMARY_RELEASE_YEAR_ORDER = "discoverRELEASE_ORDER";
    private static final String KEY_DISCOVER_PRIMARY_RELEASE_YEAR_ORDER_POSITION = "discoverRELEASE_ORDER_POS";

    private static final String KEY_DISCOVER_SORT_BY = "discoverSORT";
    private static final String KEY_DISCOVER_SORT_BY_POSITION = "discoverSORT_POS";

    private static final String KEY_DISCOVER_VOTE_AVERAGE = "discoverVOTE_AVG";
    private static final String KEY_DISCOVER_VOTE_AVERAGE_POSITION = "discoverVOTE_AVG_POS";

    private static final String KEY_DISCOVER_VOTE_AVERAGE_ORDER = "discoverVOTE_ORDER";
    private static final String KEY_DISCOVER_VOTE_AVERAGE_ORDER_POSITION = "discoverVOTE_ORDER_POS";

    private static final String KEY_DISCOVER_GENRES = "discoverGENRES";
    private static final String KEY_DISCOVER_GENRES_POSITION = "discoverGENRES_POS";

    private static final String KEY_DISCOVER_PEOPLES_NAME = "discoverPEOPLE";
    private static final String KEY_DISCOVER_PEOPLES_ID = "discoverPEOPLE_ID";


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

    //******** release year
    public void setReleaseYear(String year, int position) {
        sharedPref.edit().putString(KEY_DISCOVER_PRIMARY_RELEASE_YEAR, year)
                .putInt(KEY_DISCOVER_PRIMARY_RELEASE_YEAR_POSITION, position).apply();
    }

    public String getReleaseYear() {
        return sharedPref.getString(KEY_DISCOVER_PRIMARY_RELEASE_YEAR, null);
    }

    public int getReleaseYearPos() {
        return sharedPref.getInt(KEY_DISCOVER_PRIMARY_RELEASE_YEAR_POSITION, 0);
    }

    //******** release order
    public void setReleaseOrder(String releaseOrder, int position) {
        sharedPref.edit().putString(KEY_DISCOVER_PRIMARY_RELEASE_YEAR_ORDER, releaseOrder)
                .putInt(KEY_DISCOVER_PRIMARY_RELEASE_YEAR_ORDER_POSITION, position).apply();
    }

    public String getReleaseOrder() {
        return sharedPref.getString(KEY_DISCOVER_PRIMARY_RELEASE_YEAR_ORDER, "None");
    }

    public int getReleaseOrderPos() {
        return sharedPref.getInt(KEY_DISCOVER_PRIMARY_RELEASE_YEAR_ORDER_POSITION, 0);
    }

    //********* release sortBy
    public void setSortOrder(String sortOrder, int position) {
        sharedPref.edit().putString(KEY_DISCOVER_SORT_BY, sortOrder)
                .putInt(KEY_DISCOVER_SORT_BY_POSITION, position).apply();
    }

    public String getSortOrder() {
        return sharedPref.getString(KEY_DISCOVER_SORT_BY, "None");
    }

    public int getSortOrderPos() {
        return sharedPref.getInt(KEY_DISCOVER_SORT_BY_POSITION, 0);
    }


    //********* vote average
    public void setVoteAvg(String voteAvg, int position) {
        sharedPref.edit().putString(KEY_DISCOVER_VOTE_AVERAGE, voteAvg)
                .putInt(KEY_DISCOVER_VOTE_AVERAGE_POSITION, position).apply();
    }

    public String getVoteAvg() {
        return sharedPref.getString(KEY_DISCOVER_VOTE_AVERAGE, null);
    }

    public int getVoteAvgPos() {
        return sharedPref.getInt(KEY_DISCOVER_VOTE_AVERAGE_POSITION, 0);
    }

    // ************ vote average order
    public void setVoteOrder(String voteOrder, int position) {
        sharedPref.edit().putString(KEY_DISCOVER_VOTE_AVERAGE_ORDER, voteOrder)
                .putInt(KEY_DISCOVER_VOTE_AVERAGE_ORDER_POSITION, position).apply();
    }

    public String getVoteOrder() {
        return sharedPref.getString(KEY_DISCOVER_VOTE_AVERAGE_ORDER, "None");
    }

    public int getVoteOrderPos() {
        return sharedPref.getInt(KEY_DISCOVER_VOTE_AVERAGE_ORDER_POSITION, 0);
    }

    //****************** genres  ********************

    public void setGenres(String genres, int position) {
        sharedPref.edit().putString(KEY_DISCOVER_GENRES, genres)
                .putInt(KEY_DISCOVER_GENRES_POSITION, position).apply();
    }

    public String getGenres() {
        return sharedPref.getString(KEY_DISCOVER_GENRES, "None");
    }

    public int getGenresPos() {
        return sharedPref.getInt(KEY_DISCOVER_GENRES_POSITION, 0);
    }

    //***********
    public void savePerson(String personName, int personID) {
        //add person name
        Set<String> setName = sharedPref.getStringSet(KEY_DISCOVER_PEOPLES_NAME, new HashSet<String>());
        setName.add(personName);
        sharedPref.edit().putStringSet(KEY_DISCOVER_PEOPLES_NAME, setName).apply();

        //add person id
        Set<String> setID = sharedPref.getStringSet(KEY_DISCOVER_PEOPLES_ID, new HashSet<String>());
        setID.add(String.valueOf(personID));
        sharedPref.edit().putStringSet(KEY_DISCOVER_PEOPLES_ID, setID).apply();
    }

    public String getPersonsName() {
        Set<String> nameSet = sharedPref.getStringSet(KEY_DISCOVER_PEOPLES_NAME, new HashSet<String>());
        StringBuilder result = new StringBuilder();
        for (String name : nameSet)
            result.append(name).append(",");
        return result.toString();
    }

    public String getPersonsID() {
        Set<String> idSet = sharedPref.getStringSet(KEY_DISCOVER_PEOPLES_ID, new HashSet<String>());
        StringBuilder result = new StringBuilder();
        for (String id : idSet)
            result.append(id).append(",");
        return result.toString();
    }

    //****************************
    public void resetDiscoverData() {
        sharedPref.edit().putBoolean(KEY_DISCOVER_ADULT, false)
                .putString(KEY_DISCOVER_PRIMARY_RELEASE_YEAR, null)
                .putString(KEY_DISCOVER_PRIMARY_RELEASE_YEAR_ORDER, null)
                .putString(KEY_DISCOVER_SORT_BY, null)
                .putString(KEY_DISCOVER_VOTE_AVERAGE, null)
                .putString(KEY_DISCOVER_VOTE_AVERAGE_ORDER, null)
                .putString(KEY_DISCOVER_GENRES,null)
                .putStringSet(KEY_DISCOVER_PEOPLES_NAME, null)
                .putStringSet(KEY_DISCOVER_PEOPLES_ID, null)
                .apply();
    }

    public void resetPersonsData() {
        sharedPref.edit()
                .putStringSet(KEY_DISCOVER_PEOPLES_NAME, null)
                .putStringSet(KEY_DISCOVER_PEOPLES_ID, null)
                .apply();
    }

    //delete user from prefs
    public void deletePerson(String personName, int personID) {
        //delete person name
        Set<String> setName = sharedPref.getStringSet(KEY_DISCOVER_PEOPLES_NAME, new HashSet<String>());
        setName.remove(personName);
        sharedPref.edit().putStringSet(KEY_DISCOVER_PEOPLES_NAME, setName).apply();

        //delete person id
        Set<String> setID = sharedPref.getStringSet(KEY_DISCOVER_PEOPLES_ID, new HashSet<String>());
        setID.remove(String.valueOf(personID));
        sharedPref.edit().putStringSet(KEY_DISCOVER_PEOPLES_ID, setID).apply();
    }

}
