package kat.android.com.movielist.rest.pojo.movie;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

//Using GSON to convert json to java object
public class MovieResponse implements Serializable {

    @SerializedName("page")
    private int page;

    @SerializedName("total_pages")
    private int total_pages;

    @SerializedName("results")
    private List<Movie> movies;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }
}
