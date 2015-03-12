package kat.android.com.movielist.fragments.tabs;


import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.List;

import kat.android.com.movielist.DetailActivity;
import kat.android.com.movielist.MovieListActivity;
import kat.android.com.movielist.R;
import kat.android.com.movielist.common.PersonAdapter;
import kat.android.com.movielist.rest.RestClient;
import kat.android.com.movielist.rest.pojo.person.Person;
import kat.android.com.movielist.rest.pojo.person.PersonResult;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PeopleFragmentTab extends ListFragment implements View.OnClickListener, SearchView.OnQueryTextListener {


    private List<Person> searchResultMovies;
    private SearchView mSearchView;
    private Button mDoneButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MovieListActivity) getActivity()).getSupportActionBar().hide();
        setRetainInstance(true);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.people_search_layout, container, false);

        mDoneButton = (Button) v.findViewById(R.id.doneButton);
        mDoneButton.setOnClickListener(this);

        mSearchView = (SearchView) v.findViewById(R.id.peopleSearchView);
        mSearchView.setQueryHint("Search...");
        mSearchView.setFocusable(true);
        mSearchView.setIconified(false);
        mSearchView.setOnQueryTextListener(this);

        return v;
    }


    //GET REQUEST
    public void searchPerson(String personName) {

        RestClient.get().getPerson(personName, new Callback<PersonResult>() {
            @Override
            public void success(PersonResult personResult, Response response) {
                Toast.makeText(getActivity(), "Count " + personResult.getResults().size(), Toast.LENGTH_SHORT).show();
                searchResultMovies = personResult.getResults();
                setListAdapter(new PersonAdapter(getActivity(), searchResultMovies));
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(DetailActivity.TAG, "An error occurred while searching person info.");
            }
        });
    }

    //onItemClick

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.doneButton) {
            getFragmentManager().beginTransaction()
                    .remove(getFragmentManager().findFragmentById(R.id.fragment_discover_data_list))
                    .show(getFragmentManager().findFragmentById(R.id.fragment_discover))
                    .commit();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String text) {
        if (text.length() > 0) {
            searchPerson(text);
        }
        if (text.length() == 0) setListAdapter(null);
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((MovieListActivity) getActivity()).getSupportActionBar().show();
    }
}
