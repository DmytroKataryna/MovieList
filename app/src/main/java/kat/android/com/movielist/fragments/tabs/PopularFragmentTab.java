package kat.android.com.movielist.fragments.tabs;

import android.util.Log;

import kat.android.com.movielist.DetailActivity;
import kat.android.com.movielist.MovieListActivity;
import kat.android.com.movielist.rest.RestClient;
import kat.android.com.movielist.rest.pojo.movie.MovieResponse;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

//fragment which contains popular movie list
public class PopularFragmentTab extends AbstractFragmentTab {


    public void loadData(int page) {
        RestClient.get().getPopularMovies(page, new Callback<MovieResponse>() {
            @Override
            public void success(MovieResponse movieResponse, Response response) {
                //get movies list , and add it to array
                movieList.addAll(movieResponse.getMovies());
                totalPages = movieResponse.getTotal_pages();

                if (listView.getAdapter() == null)
                    listView.setAdapter(adapter);
                else
                    adapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(DetailActivity.TAG, "An error occurred while downloading popular movies list.");
            }
        });
    }

    //on hide/show fragment listener
    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden)
            ((MovieListActivity) getActivity()).getSupportActionBar().setTitle("Popular");
    }
}
