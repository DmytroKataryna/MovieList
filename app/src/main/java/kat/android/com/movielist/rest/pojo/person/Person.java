package kat.android.com.movielist.rest.pojo.person;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import kat.android.com.movielist.rest.pojo.movie.Movie;

//Using GSON to convert json to java object
public class Person implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("adult")
    private boolean adult;

    @SerializedName("known_for")
    private List<Movie> known_for;

    @SerializedName("popularity")
    private float popularity;

    @SerializedName("name")
    private String name;

    @SerializedName("profile_path")
    private String profile_path;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public List<Movie> getKnown_for() {
        return known_for;
    }

    public void setKnown_for(List<Movie> known_for) {
        this.known_for = known_for;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public void setProfile_path(String profile_path) {
        this.profile_path = profile_path;
    }
}
