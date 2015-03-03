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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;

import java.util.List;

import kat.android.com.movielist.DetailActivity;
import kat.android.com.movielist.common.MovieAdapter;
import kat.android.com.movielist.R;
import kat.android.com.movielist.rest.RestClient;
import kat.android.com.movielist.rest.pojo.movie.Movie;
import kat.android.com.movielist.rest.pojo.movie.MovieResponse;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SearchFragmentTab extends Fragment implements AdapterView.OnItemClickListener, SearchView.OnQueryTextListener {

    private List<Movie> searchResultMovies;
    private ListView listView;
    private ProgressBar progressBar;
    private SearchView searchView;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.search_tab_layout, container, false);
            progressBar = (ProgressBar) view.findViewById(R.id.searchProgressBar);

            searchView = (SearchView) view.findViewById(R.id.search);
            searchView.setIconifiedByDefault(false);
            searchView.setOnQueryTextListener(this);

            //Трохи костиль , тут я замінюю стандартне підкреслення SearchView з синього кольору на оранджевий
            //елегантнішого вирішення не знайшов )
            int searchPlateId = searchView.getContext().getResources()
                    .getIdentifier("android:id/search_plate", null, null);
            View searchPlateView = searchView.findViewById(searchPlateId);
            if (searchPlateView != null) {
                searchPlateView.setBackgroundResource(R.drawable.texfield_searchview_holo_light);
            }

            listView = (ListView) view.findViewById(R.id.list);
            listView.setOnItemClickListener(this);
        } else {
            // If we are returning from a configuration change:
            // "view" is still attached to the previous view hierarchy
            // so we need to remove it and re-attach it to the current one
            ViewGroup parent = (ViewGroup) view.getParent();
            parent.removeView(view);
        }
        return view;
    }

    //search just , if text field is not empty
    // if empty clear list
    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.length() > 0) {
            progressBar.setVisibility(View.VISIBLE);
            searchMovies(newText);
        }
        if (newText.length() == 0) listView.setAdapter(null);
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    public void searchMovies(String movieName) {

        RestClient.get().searchMovies(movieName, new Callback<MovieResponse>() {
            @Override
            public void success(MovieResponse movieResponse, Response response) {
                //get movies list , and add it to array
                searchResultMovies = movieResponse.getMovies();
                listView.setAdapter(new MovieAdapter(getActivity(), searchResultMovies));
                progressBar.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(DetailActivity.TAG, "An error occurred while searching movies info.");
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(getActivity(), DetailActivity.class)
                .putExtra("KEY", searchResultMovies.get(position).getId());
        startActivity(i);
    }
}
