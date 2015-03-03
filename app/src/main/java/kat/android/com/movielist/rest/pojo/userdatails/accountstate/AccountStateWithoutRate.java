package kat.android.com.movielist.rest.pojo.userdatails.accountstate;

import com.google.gson.annotations.SerializedName;

//Using GSON to convert json to java object
public class AccountStateWithoutRate {

    @SerializedName("id")
    private int id;

    @SerializedName("favorite")
    private boolean favorite;

    @SerializedName("watchlist")
    private boolean watchlist;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public boolean isWatchlist() {
        return watchlist;
    }

    public void setWatchlist(boolean watchlist) {
        this.watchlist = watchlist;
    }
}
