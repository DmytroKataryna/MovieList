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
import kat.android.com.movielist.common.EndlessScrollListener;
import kat.android.com.movielist.common.MovieAdapter;
import kat.android.com.movielist.R;
import kat.android.com.movielist.rest.RestClient;
import kat.android.com.movielist.rest.pojo.Movie;
import kat.android.com.movielist.rest.pojo.MovieResponse;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

//fragment which contains upcoming movie list
public class UpcomingFragmentTab extends Fragment implements AdapterView.OnItemClickListener {

    private static int currentPage = 1;

    List<Movie> upcomingMovies = new ArrayList<>();
    ListView listView;
    ProgressBar progressBar;
    BaseAdapter adapter;
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        loadData(currentPage);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.tab_layout, container, false);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            adapter = new MovieAdapter(getActivity(), upcomingMovies);

            listView = (ListView) view.findViewById(R.id.listView);
            listView.setOnItemClickListener(this);
            listView.setAdapter(adapter);
            //implements custom AbsListView.OnScrollListener
            listView.setOnScrollListener(new EndlessScrollListener() {
                //load just first four pages
                @Override
                protected boolean hasMoreDataToLoad() {
                    return currentPage < 4;
                }

                @Override
                protected void loadMoreData(int page) {
                    currentPage = page;
                    loadData(currentPage);
                }
            });
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
                .putExtra("KEY", upcomingMovies.get(position).getId());
        startActivity(i);

    }

    public void loadData(int page) {
        RestClient.get().getUpcomingMovies(page, new Callback<MovieResponse>() {
            @Override
            public void success(MovieResponse movieResponse, Response response) {
                //get movies list , and add it to array
                upcomingMovies.addAll(movieResponse.getMovies());
                progressBar.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(DetailActivity.TAG, "An error occurred while downloading popular movies list.");
            }
        });
    }
}
