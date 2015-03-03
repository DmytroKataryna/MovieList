package kat.android.com.movielist.rest.pojo.userdatails.post;

import java.io.Serializable;

//Http POST Body
public class WatchList implements Serializable {

    private String media_type;
    private Integer media_id;
    private Boolean watchlist;

    public WatchList(String media_type, Integer media_id, Boolean watchlist) {
        this.media_type = media_type;
        this.media_id = media_id;
        this.watchlist = watchlist;
    }

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    public Integer getMedia_id() {
        return media_id;
    }

    public void setMedia_id(Integer media_id) {
        this.media_id = media_id;
    }

    public Boolean getWatchlist() {
        return watchlist;
    }

    public void setWatchlist(Boolean watchlist) {
        this.watchlist = watchlist;
    }
}
