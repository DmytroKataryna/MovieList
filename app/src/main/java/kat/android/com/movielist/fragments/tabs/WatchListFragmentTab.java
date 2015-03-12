package kat.android.com.movielist.fragments.tabs;


import android.util.Log;
import android.view.View;

import kat.android.com.movielist.DetailActivity;
import kat.android.com.movielist.common.PreferencesUtils;
import kat.android.com.movielist.rest.RestClient;
import kat.android.com.movielist.rest.pojo.movie.MovieResponse;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class WatchListFragmentTab extends AbstractFragmentTab {

    private PreferencesUtils utils;


    //load first page of favorite movies
    public void loadData(int page) {
        utils = PreferencesUtils.get(getActivity());

        RestClient.get().getWatchListMovies(utils.getSessionUserID(), utils.getSessionID(), page, new Callback<MovieResponse>() {
            @Override
            public void success(MovieResponse movieResponse, Response response) {
                //get movies list , and add it to array
                movieList.addAll(movieResponse.getMovies());
                totalPages = movieResponse.getTotal_pages();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(DetailActivity.TAG, "An error occurred while downloading favorites movies list.");
            }
        });
    }
}
