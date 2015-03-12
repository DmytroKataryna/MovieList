package kat.android.com.movielist.fragments.tabs;


import android.util.Log;
import android.view.Menu;
import android.view.View;

import kat.android.com.movielist.DetailActivity;
import kat.android.com.movielist.R;
import kat.android.com.movielist.rest.RestClient;
import kat.android.com.movielist.rest.pojo.movie.MovieResponse;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

//fragment which contains top rated movie list
public class TopRatedFragmentTab extends AbstractFragmentTab {


    public void loadData(int page) {
        RestClient.get().getTopRatedMovies(page, new Callback<MovieResponse>() {
            @Override
            public void success(MovieResponse movieResponse, Response response) {
                //get movies list , and add it to array
                movieList.addAll(movieResponse.getMovies());
                listView.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(DetailActivity.TAG, "An error occurred while downloading popular movies list.");
            }
        });
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.txt).setVisible(false);
        menu.add("XXz");
    }
}
