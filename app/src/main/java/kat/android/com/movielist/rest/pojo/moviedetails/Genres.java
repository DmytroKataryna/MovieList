package kat.android.com.movielist.rest.pojo.moviedetails;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

//Using GSON to convert json to java object
public class Genres implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
