package kat.android.com.movielist.fragments.tabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.util.Calendar;
import java.util.Date;

import kat.android.com.movielist.MovieListActivity;
import kat.android.com.movielist.R;
import kat.android.com.movielist.common.PreferencesUtils;

public class DiscoverFragmentTab extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {


    private PreferencesUtils utils;
    private Spinner mYearSpinner, mSortSpinner, mYearOrderSpinner, mRatingSpinner, mRatingOrderSpinner, mGenresSpinner;
    private EditText mPeopleEditText;
    private CheckBox mAdultCheck;
    private ImageButton mResetPeopleButton;

    private String years[] = new String[99];
    private static final int CURRENT_YEAR = Calendar.getInstance().get(Calendar.YEAR);


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        utils = PreferencesUtils.get(getActivity());

        //init years
        initYearArray();
        //clear person data
        utils.resetPersonsData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.discover_search_layout, container, false);

        mAdultCheck = (CheckBox) v.findViewById(R.id.adultCheckBox);
        mAdultCheck.setOnCheckedChangeListener(this);
        mYearSpinner = (Spinner) v.findViewById(R.id.yearSpinner);
        mYearSpinner.setOnItemSelectedListener(this);
        mYearOrderSpinner = (Spinner) v.findViewById(R.id.yearOrderSpinner);
        mYearOrderSpinner.setOnItemSelectedListener(this);
        mSortSpinner = (Spinner) v.findViewById(R.id.sortBySpinner);
        mSortSpinner.setOnItemSelectedListener(this);
        mRatingOrderSpinner = (Spinner) v.findViewById(R.id.ratingOrderSpinner);
        mRatingOrderSpinner.setOnItemSelectedListener(this);
        mRatingSpinner = (Spinner) v.findViewById(R.id.ratingSpinner);
        mRatingSpinner.setOnItemSelectedListener(this);
        mGenresSpinner = (Spinner) v.findViewById(R.id.genresSpinner);
        mGenresSpinner.setOnItemSelectedListener(this);

        mPeopleEditText = (EditText) v.findViewById(R.id.peopleEditText);
        mPeopleEditText.setOnClickListener(this);
        mResetPeopleButton = (ImageButton) v.findViewById(R.id.imagePeopleButton);
        mResetPeopleButton.setOnClickListener(this);

        ArrayAdapter<String> yearsAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, years);
        yearsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> orderAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.year_order_array, android.R.layout.simple_spinner_item);
        orderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> sortByAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.sort_by_array, android.R.layout.simple_spinner_item);
        sortByAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> ratingAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.rating_array, android.R.layout.simple_spinner_item);
        sortByAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> genresAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.genres_array, android.R.layout.simple_spinner_item);
        sortByAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        mYearSpinner.setAdapter(yearsAdapter);
        mYearOrderSpinner.setAdapter(orderAdapter);
        mSortSpinner.setAdapter(sortByAdapter);
        mRatingOrderSpinner.setAdapter(orderAdapter);
        mRatingSpinner.setAdapter(ratingAdapter);
        mGenresSpinner.setAdapter(genresAdapter);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.peopleEditText:
                //show people detail fragment (in which user picks persons  )
                getFragmentManager().beginTransaction()
                        .hide(getFragmentManager().findFragmentById(R.id.fragment_discover))
                        .replace(R.id.fragment_discover_data_list, new PeopleFragmentTab())
                        .commit();
                break;
            case R.id.imagePeopleButton:
                utils.resetPersonsData();
                mPeopleEditText.setText("");
                break;
        }
    }

    //spinner listener . storage data to preferences
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.yearSpinner:
                //save year spinner text and position to preferences
                utils.setReleaseYear(parent.getItemAtPosition(position).toString(), position);
                break;
            case R.id.yearOrderSpinner:
                //save year spinner order text and position to preferences
                utils.setReleaseOrder(parent.getItemAtPosition(position).toString(), position);
                break;
            case R.id.sortBySpinner:
                //save sort spinner text and position to preferences
                utils.setSortOrder(parent.getItemAtPosition(position).toString(), position);
                break;
            case R.id.ratingSpinner:
                //save rating spinner text and position to preferences
                utils.setVoteAvg(parent.getItemAtPosition(position).toString(), position);
                break;
            case R.id.ratingOrderSpinner:
                //save rating  order spinner text and position to preferences
                utils.setVoteOrder(parent.getItemAtPosition(position).toString(), position);
                break;
            case R.id.genresSpinner:
                //save genres spinner text and position to preferences
                utils.setGenres(parent.getItemAtPosition(position).toString(), position);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //checkbox listener
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        utils.setAdult(isChecked);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        //restore discover data from preferences
        if (!hidden) {
            ((MovieListActivity) getActivity()).getSupportActionBar().setTitle("Discover");
            mAdultCheck.setChecked(utils.isAdult());
            mYearSpinner.setSelection(utils.getReleaseYearPos());
            mYearOrderSpinner.setSelection(utils.getReleaseOrderPos());
            mSortSpinner.setSelection(utils.getSortOrderPos());
            mRatingSpinner.setSelection(utils.getVoteAvgPos());
            mRatingOrderSpinner.setSelection(utils.getVoteOrderPos());
            mGenresSpinner.setSelection(utils.getGenresPos());
            mPeopleEditText.setText(utils.getPersonsName());

        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.search).setVisible(false);
        menu.findItem(R.id.done).setVisible(true);
        menu.findItem(R.id.reset).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            //reset menu item
            case R.id.reset:
                mAdultCheck.setChecked(false);
                mYearSpinner.setSelection(0);
                mYearOrderSpinner.setSelection(0);
                mSortSpinner.setSelection(0);
                mRatingSpinner.setSelection(0);
                mRatingOrderSpinner.setSelection(0);
                mGenresSpinner.setSelection(0);
                mPeopleEditText.setText("");

                //clear data in preferences
                utils.resetDiscoverData();
                break;

            // confirm menu item
            case R.id.done:
                getFragmentManager().beginTransaction()
                        .hide(getFragmentManager().findFragmentById(R.id.fragment_discover))
                        .replace(R.id.fragment_discover_data_list, new DiscoverResultFragmentTab())
                        .commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //init array which contains strings (years from 2015 till 1910) and first elem "None"
    private void initYearArray() {
        for (int i = 0; i < years.length; i++) {
            if (i == 0)
                years[i] = "None";
            else
                // + 1 because first element should be None and then 2015(current year)
                years[i] = String.valueOf(CURRENT_YEAR - i + 1);
        }
    }
}
