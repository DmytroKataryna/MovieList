package kat.android.com.movielist.rest.pojo.userdatails;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AccountState implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("favorite")
    private boolean favorite;


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
