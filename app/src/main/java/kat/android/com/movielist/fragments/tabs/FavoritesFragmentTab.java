package kat.android.com.movielist.fragments.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import kat.android.com.movielist.DetailActivity;
import kat.android.com.movielist.R;
import kat.android.com.movielist.common.MovieAdapter;
import kat.android.com.movielist.common.PreferencesUtils;
import kat.android.com.movielist.rest.RestClient;
import kat.android.com.movielist.rest.pojo.movie.Movie;
import kat.android.com.movielist.rest.pojo.movie.MovieResponse;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class FavoritesFragmentTab extends Fragment implements AdapterView.OnItemClickListener {

    private static int currentPage = 1;
    private PreferencesUtils utils;

    List<Movie> favoritesMovies = new ArrayList<>();
    ListView listView;
    ProgressBar progressBar;
    BaseAdapter adapter;
    View view;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        //utils class which stores user data (login , session , name)
        utils = PreferencesUtils.get(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        //load user favorite movie list
        loadData(currentPage);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.tab_layout, container, false);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            adapter = new MovieAdapter(getActivity(), favoritesMovies);

            listView = (ListView) view.findViewById(R.id.listView);
            listView.setOnItemClickListener(this);
            listView.setAdapter(adapter);
        } else {
            // If we are returning from a configuration change:
            // "view" is still attached to the previous view hierarchy
            // so we need to remove it and re-attach it to the current one
            ViewGroup parent = (ViewGroup) view.getParent();
            parent.removeView(view);
        }
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(getActivity(), DetailActivity.class)
                .putExtra("KEY", favoritesMovies.get(position).getId());
        startActivity(i);
    }

    //load first page of favorite movies
    public void loadData(int page) {
        RestClient.get().getFavoritesMovies(utils.getSessionUserID(), utils.getSessionID(), 1, new Callback<MovieResponse>() {
            @Override
            public void success(MovieResponse movieResponse, Response response) {
                favoritesMovies.clear();
                favoritesMovies.addAll(movieResponse.getMovies());
                progressBar.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(DetailActivity.TAG, "An error occurred while downloading favorites movies list.");
            }
        });
    }
}
