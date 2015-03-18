package kat.android.com.movielist.fragments.tabs;


import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.SearchView;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.OnClickWrapper;
import com.melnykov.fab.FloatingActionButton;
import com.quentindommerc.superlistview.OnMoreListener;
import com.quentindommerc.superlistview.SuperListview;

import java.util.ArrayList;
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

public class PeopleFragmentTab extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private PreferencesUtils utils;

    private int currentPage = 1;
    private int totalPages = 1;

    private String personName;
    private int personId;

    private List<Person> searchResultMovies = new ArrayList<>();
    private SearchView mSearchView;
    private FloatingActionButton mDoneButton;
    private SuperListview listView;
    private SuperActivityToast superActivityToast;
    private BaseAdapter adapter;

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
    public void onResume() {
        super.onResume();
        //load popular person list
        popularPersons(currentPage);

        //hide soft keyboard when user click on ListView
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(v);
                return false;
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.people_search_layout, container, false);
        adapter = new PersonAdapter(getActivity(), searchResultMovies);

        listView = (SuperListview) v.findViewById(R.id.listPeoples);
        listView.setOnItemClickListener(this);
        listView.setAdapter(adapter);

        listView.setupMoreListener(new OnMoreListener() {
            @Override
            public void onMoreAsked(int numberOfItems, int numberBeforeMore, int currentItemPos) {
                listView.showMoreProgress();
                if (currentPage < totalPages)
                    popularPersons(++currentPage);
                listView.hideMoreProgress();
            }
        }, 5);

        mDoneButton = (FloatingActionButton) v.findViewById(R.id.fab);
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

                if (newText.length() == 0) {
                    listView.clear();
                    loadPopularPersonFirstPage();
                }
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
                searchResultMovies.clear();
                searchResultMovies = personResult.getResults();
                listView.setAdapter(new PersonAdapter(getActivity(), searchResultMovies));
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(DetailActivity.TAG, "An error occurred while searching person info.");
            }
        });
    }

    //GET REQUEST popular persons
    public void popularPersons(int page) {

        RestClient.get().getPopularPerson(page, new Callback<PersonResult>() {
            @Override
            public void success(PersonResult personResult, Response response) {
                totalPages = personResult.getTotal_pages();
                searchResultMovies.addAll(personResult.getResults());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(DetailActivity.TAG, "An error occurred while getting popular person list.");
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


    //keyboard
    protected void hideKeyboard(View view) {
        InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    //clear list and load first page
    protected void loadPopularPersonFirstPage() {
        searchResultMovies.clear();
        currentPage = 1;
        popularPersons(currentPage);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((MovieListActivity) getActivity()).getSupportActionBar().show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        personName = searchResultMovies.get(position).getName();
        personId = searchResultMovies.get(position).getId();
        //save person to utils
        utils.savePerson(personName, personId);

        //close previous toast if it is shown
        if (superActivityToast != null && superActivityToast.isShowing())
            superActivityToast.dismiss();

        //toast
        superActivityToast = new SuperActivityToast(getActivity(), SuperToast.Type.BUTTON);
        superActivityToast.setDuration(SuperToast.Duration.SHORT);
        superActivityToast.setText(searchResultMovies.get(position).getName() + " Added");
        superActivityToast.setButtonIcon(SuperToast.Icon.Dark.UNDO, "UNDO");
        superActivityToast.setButtonTextColor(getResources().getColor(R.color.material_drawer_primary_text));
        superActivityToast.setTextColor(getResources().getColor(R.color.material_drawer_primary_text));
        superActivityToast.setOnClickWrapper(onClickWrapper);
        superActivityToast.show();

        //when user selects a person , Done Button should be visible
        mDoneButton.show(true);
    }

    //toast listener
    OnClickWrapper onClickWrapper = new OnClickWrapper("superToast", new SuperToast.OnClickListener() {
        @Override
        public void onClick(View view, Parcelable parcelable) {
            //delete person from preferences if user click on Undo Button
            utils.deletePerson(personName, personId);
        }
    });
}
