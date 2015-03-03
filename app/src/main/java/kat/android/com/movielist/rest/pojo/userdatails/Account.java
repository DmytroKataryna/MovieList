package kat.android.com.movielist.rest.pojo.userdatails;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Account implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("include_adult")
    private boolean include_adult;

    @SerializedName("iso_3166_1")
    private String iso_3166_1;

    @SerializedName("iso_639_1")
    private String iso_639_1;

    @SerializedName("name")
    private String name;

    @SerializedName("username")
    private String username;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isInclude_adult() {
        return include_adult;
    }

    public void setInclude_adult(boolean include_adult) {
        this.include_adult = include_adult;
    }

    public String getIso_3166_1() {
        return iso_3166_1;
    }

    public void setIso_3166_1(String iso_3166_1) {
        this.iso_3166_1 = iso_3166_1;
    }

    public String getIso_639_1() {
        return iso_639_1;
    }

    public void setIso_639_1(String iso_639_1) {
        this.iso_639_1 = iso_639_1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
