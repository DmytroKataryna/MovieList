package kat.android.com.movielist.fragments.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import kat.android.com.movielist.DetailActivity;
import kat.android.com.movielist.R;
import kat.android.com.movielist.common.MovieAdapter;
import kat.android.com.movielist.rest.RestClient;
import kat.android.com.movielist.rest.pojo.movie.Movie;
import kat.android.com.movielist.rest.pojo.movie.MovieResponse;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SearchFragmentTab extends ListFragment {

    private List<Movie> searchResultMovies;
    private SearchView searchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem searchItem = menu.findItem(R.id.search);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("Search...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 0) {
                    searchMovies(newText);
                }
                if (newText.length() == 0) setListAdapter(null);
                return true;
            }
        });
    }


    //GET REQUEST
    public void searchMovies(String movieName) {

        RestClient.get().searchMovies(movieName, new Callback<MovieResponse>() {
            @Override
            public void success(MovieResponse movieResponse, Response response) {
                searchResultMovies = movieResponse.getMovies();
                setListAdapter(new MovieAdapter(getActivity(), searchResultMovies));
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(DetailActivity.TAG, "An error occurred while searching movies info.");
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent i = new Intent(getActivity(), DetailActivity.class)
                .putExtra("KEY", searchResultMovies.get(position).getId());
        startActivity(i);
    }
}
