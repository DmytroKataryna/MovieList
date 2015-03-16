package kat.android.com.movielist.fragments.tabs;


import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import kat.android.com.movielist.DetailActivity;
import kat.android.com.movielist.MovieListActivity;
import kat.android.com.movielist.R;
import kat.android.com.movielist.common.PreferencesUtils;
import kat.android.com.movielist.rest.RestClient;
import kat.android.com.movielist.rest.pojo.movie.MovieResponse;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DiscoverResultFragmentTab extends AbstractFragmentTab {

    private PreferencesUtils utils;

    private boolean adult;
    private String release_year;
    private String release_order_gte;
    private String release_order_lte;
    private String sort_by;
    private String vote;
    private Float voteGTE;
    private Float voteLTE;
    private String people;

    private String year;
    private String release_order;

    //HTTP GET discover (load discover data)
    @Override
    public void loadData(int page) {
        utils = PreferencesUtils.get(getActivity());
        getRequestParameters();

        RestClient.get().getDiscoverMovies(adult, page, release_year, release_order_gte, release_order_lte, sort_by, voteGTE, voteLTE, people, new Callback<MovieResponse>() {
            @Override
            public void success(MovieResponse movieResponse, Response response) {
                movieList.addAll(movieResponse.getMovies());
                totalPages = movieResponse.getTotal_pages();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(DetailActivity.TAG, "An error occurred while downloading discover movies list.");
            }
        });
    }


    //transform data from utils to get parameters
    private void getRequestParameters() {

        adult = utils.isAdult();
        year = utils.getReleaseYear();
        vote = utils.getVoteAvg();

        //release logic
        release_order = utils.getReleaseOrder();
        switch (release_order) {
            case "None":
                release_year = year;
                release_order_gte = null;
                release_order_lte = null;
                break;
            case "Greater":
                release_order = null;
                release_order_gte = year + "-01-01";
                release_order_lte = null;
                break;
            case "Less":
                release_order = null;
                release_order_gte = null;
                release_order_lte = year + "-01-01";
                break;
        }

        //sort by
        switch (utils.getSortOrder()) {
            case "None":
                sort_by = null;
                break;
            case "Release Date asc":
                sort_by = "release_date.asc";
                break;
            case "Release Date desc":
                sort_by = "release_date.desc";
                break;
            case "Popularity asc":
                sort_by = "popularity.asc";
                break;
            case "Popularity desc":
                sort_by = "popularity.desc";
                break;
            case "Vote Average asc":
                sort_by = "vote_average.asc";
                break;
            case "Vote Average desc":
                sort_by = "vote_average.desc";
                break;
            case "Vote Count asc":
                sort_by = "vote_count.asc";
                break;
            case "Vote Count desc":
                sort_by = "vote_count.desc";
                break;
        }

        //vote average
        switch (utils.getVoteOrder()) {
            case "None":
                voteGTE = null;
                voteLTE = null;
                break;
            case "Greater":
                voteGTE = Float.valueOf(vote);
                voteLTE = null;
                break;
            case "Less":
                voteGTE = null;
                voteLTE = Float.valueOf(vote);
                break;
        }

        //people ID's
        if (utils.getPersonsID() != null && utils.getPersonsID().length() > 0)
            people = utils.getPersonsID();
        else
            people = null;


    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.search).setVisible(false);
        menu.findItem(R.id.done).setVisible(true);
        menu.findItem(R.id.reset).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.done) {
            //set position to discover tab (isn't best idea)
            MovieListActivity.drawerResult.setSelection(4);

            //show discover menu fragment
            getFragmentManager().beginTransaction()
                    .hide(getFragmentManager().findFragmentById(R.id.fragment_discover_data_list))
                    .show(getFragmentManager().findFragmentById(R.id.fragment_discover))
                    .commit();
        }

        return super.onOptionsItemSelected(item);
    }


}
