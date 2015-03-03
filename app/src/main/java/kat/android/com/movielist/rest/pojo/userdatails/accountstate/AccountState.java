package kat.android.com.movielist.rest.pojo.userdatails.accountstate;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

//Using GSON to convert json to java object
public class AccountState implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("favorite")
    private boolean favorite;

    @SerializedName("watchlist")
    private boolean watchlist;

//    @SerializedName("rated")
//    private Rated rated;
//
//    public Rated getRated() {
//        return rated;
//    }
//
//    public void setRated(Rated rated) {
//        this.rated = rated;
//    }

    public boolean isWatchlist() {
        return watchlist;
    }

    public void setWatchlist(boolean watchlist) {
        this.watchlist = watchlist;
    }

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
}
