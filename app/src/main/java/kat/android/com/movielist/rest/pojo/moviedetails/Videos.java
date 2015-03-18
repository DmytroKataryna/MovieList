package kat.android.com.movielist.rest.pojo.moviedetails;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Videos implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("results")
    private List<Results> results;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Results> getResults() {
        return results;
    }

    public void setResults(List<Results> results) {
        this.results = results;
    }
}
