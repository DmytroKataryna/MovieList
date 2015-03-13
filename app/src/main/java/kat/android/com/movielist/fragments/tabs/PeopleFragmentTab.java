package kat.android.com.movielist.fragments.tabs;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.OnClickWrapper;
import com.melnykov.fab.FloatingActionButton;

import java.util.List;

import kat.android.com.movielist.DetailActivity;
import kat.android.com.movielist.MovieListActivity;
import kat.android.com.movielist.R;
import kat.android.com.movielist.common.PersonAdapter;
import kat.android.com.movielist.common.PreferencesUtils;
import kat.android.com.movielist.rest.RestClient;
import kat.android.com.movielist.rest.pojo.person.Person;
import kat.android.com.movielist.rest.pojo.person.PersonResult;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PeopleFragmentTab extends ListFragment implements View.OnClickListener {

    private String personName;
    private int personId;

    private PreferencesUtils utils;
    private List<Person> searchResultMovies;
    private SearchView mSearchView;
    private FloatingActionButton mDoneButton;
    private ListView listView;
    private SuperActivityToast superActivityToast;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide action bar
        ((MovieListActivity) getActivity()).getSupportActionBar().hide();
        utils = PreferencesUtils.get(getActivity());
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.people_search_layout, container, false);

        listView = (ListView) v.findViewById(android.R.id.list);
        mDoneButton = (FloatingActionButton) v.findViewById(R.id.fab);
        mDoneButton.attachToListView(listView);
        mDoneButton.setOnClickListener(this);

        mSearchView = (SearchView) v.findViewById(R.id.peopleSearchView);
        //change searchView line color
        int searchPlate = getResources().getIdentifier("android:id/search_plate", null, null);
        mSearchView.findViewById(searchPlate).setBackgroundResource(R.drawable.texfield_searchview_holo_light);

        mSearchView.setQueryHint("Search...");
        mSearchView.setFocusable(true);
        mSearchView.setIconified(false);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 0)
                    searchPerson(newText);

                if (newText.length() == 0)
                    setListAdapter(null);
                return true;
            }
        });

        return v;
    }

    //GET REQUEST (return person data)
    public void searchPerson(String personName) {

        RestClient.get().getPerson(personName, new Callback<PersonResult>() {
            @Override
            public void success(PersonResult personResult, Response response) {
                searchResultMovies = personResult.getResults();
                setListAdapter(new PersonAdapter(getActivity(), searchResultMovies));
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(DetailActivity.TAG, "An error occurred while searching person info.");
            }
        });
    }

    //on Floating Button listener
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab) {
            //close toast before redirection to Discover fragment
            if (superActivityToast != null && superActivityToast.isShowing())
                superActivityToast.dismiss();

            getFragmentManager().beginTransaction()
                    .remove(getFragmentManager().findFragmentById(R.id.fragment_discover_data_list))
                    .show(getFragmentManager().findFragmentById(R.id.fragment_discover))
                    .commit();
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        personName = searchResultMovies.get(position).getName();
        personId = searchResultMovies.get(position).getId();
        //save person to utils
        utils.savePerson(personName, personId);

        //close previous toast if it is shown
        if (superActivityToast != null && superActivityToast.isShowing())
            superActivityToast.dismiss();

        //toast
        superActivityToast = new SuperActivityToast(getActivity(), SuperToast.Type.BUTTON);
        superActivityToast.setDuration(SuperToast.Duration.VERY_SHORT);
        superActivityToast.setText(searchResultMovies.get(position).getName() + " Added");
        superActivityToast.setButtonIcon(SuperToast.Icon.Dark.UNDO, "UNDO");
        superActivityToast.setButtonTextColor(getResources().getColor(R.color.material_drawer_primary_text));
        superActivityToast.setTextColor(getResources().getColor(R.color.material_drawer_primary_text));
        superActivityToast.setOnClickWrapper(onClickWrapper);
        superActivityToast.show();

        mDoneButton.show(true);
    }

    //toast listener
    OnClickWrapper onClickWrapper = new OnClickWrapper("superToast", new SuperToast.OnClickListener() {
        @Override
        public void onClick(View view, Parcelable parcelable) {
            Log.d("PERSON", "UNDO CLICK");
            //delete person from preferences if user click on Undo Button
            utils.deletePerson(personName, personId);
        }
    });

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((MovieListActivity) getActivity()).getSupportActionBar().show();
    }
}
