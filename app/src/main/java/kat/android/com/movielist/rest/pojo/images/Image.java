package kat.android.com.movielist.rest.pojo.images;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Image implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("backdrops")
    private List<Backdrop> backdrops;

    @SerializedName("posters")
    private List<Poster> posters;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Backdrop> getBackdrops() {
        return backdrops;
    }

    public void setBackdrops(List<Backdrop> backdrops) {
        this.backdrops = backdrops;
    }

    public List<Poster> getPosters() {
        return posters;
    }

    public void setPosters(List<Poster> posters) {
        this.posters = posters;
    }
}
