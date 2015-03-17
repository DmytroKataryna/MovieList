package kat.android.com.movielist.rest.pojo.images;


import com.google.gson.annotations.SerializedName;

public class Poster {

    @SerializedName("file_path")
    private String file_path;

    @SerializedName("vote_average")
    private Float vote_average;

    @SerializedName("vote_count")
    private int vote_count;

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public Float getVote_average() {
        return vote_average;
    }

    public void setVote_average(Float vote_average) {
        this.vote_average = vote_average;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }
}
