package kat.android.com.movielist.rest.pojo.moviedetails;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

//Using GSON to convert json to java object
public class MovieDetails implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("runtime")
    private int runtime;

    @SerializedName("budget")
    private int budget;

    @SerializedName("vote_average")
    private double vote_average;

    @SerializedName("vote_count")
    private int vote_count;

    @SerializedName("original_title")
    private String original_title;

    @SerializedName("title")
    private String title;

    @SerializedName("overview")
    private String overview;

    @SerializedName("homepage")
    private String homepage;

    @SerializedName("original_language")
    private String original_language;

    @SerializedName("backdrop_path")
    private String backdrop_path;

    @SerializedName("poster_path")
    private String poster_path;

    @SerializedName("release_date")
    private String release_date;

    @SerializedName("genres")
    private List<Genres> genres;



    public List<Genres> getGenres() {
        return genres;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public void setGenres(List<Genres> genres) {
        this.genres = genres;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }
}
