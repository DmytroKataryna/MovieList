package kat.android.com.movielist.fragments.tabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.johnpersano.supertoasts.SuperActivityToast;

import kat.android.com.movielist.MovieListActivity;
import kat.android.com.movielist.R;
import kat.android.com.movielist.common.PreferencesUtils;

public class DiscoverFragmentTab extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {


    private PreferencesUtils utils;

    private Spinner mYearSpinner, mSortSpinner, mYearOrderSpinner, mRatingSpinner, mRatingOrderSpinner;
    private EditText mPeopleEditText;
    private CheckBox mAdultCheck;

    private String years[] = new String[99];
    private static final int CURRENT_YEAR = 2015;
    private static final int UNSELECTED = 99;

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

        mPeopleEditText = (EditText) v.findViewById(R.id.peopleEditText);
        mPeopleEditText.setOnClickListener(this);

        ArrayAdapter<String> yearsAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, years);
        yearsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> orderAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.year_order_array, android.R.layout.simple_spinner_item);
        orderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> sortByAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.sort_by_array, android.R.layout.simple_spinner_item);
        sortByAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> ratingAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.rating_array, android.R.layout.simple_spinner_item);
        sortByAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        mYearSpinner.setAdapter(yearsAdapter);
        mYearOrderSpinner.setAdapter(orderAdapter);
        mSortSpinner.setAdapter(sortByAdapter);
        mRatingOrderSpinner.setAdapter(orderAdapter);
        mRatingSpinner.setAdapter(ratingAdapter);

        return v;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.peopleEditText) {
            getFragmentManager().beginTransaction()
                    .hide(getFragmentManager().findFragmentById(R.id.fragment_discover))
                    .replace(R.id.fragment_discover_data_list, new PeopleFragmentTab())
                    .commit();
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
            mAdultCheck.setChecked(utils.isAdult());
            mYearSpinner.setSelection(utils.getReleaseYearPos());
            mYearOrderSpinner.setSelection(utils.getReleaseOrderPos());
            mSortSpinner.setSelection(utils.getSortOrderPos());
            mRatingSpinner.setSelection(utils.getVoteAvgPos());
            mRatingOrderSpinner.setSelection(utils.getVoteOrderPos());
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
            case R.id.reset:
                mAdultCheck.setChecked(false);
                mYearSpinner.setSelection(0);
                mYearOrderSpinner.setSelection(0);
                mSortSpinner.setSelection(0);
                mRatingSpinner.setSelection(0);
                mRatingOrderSpinner.setSelection(0);
                mPeopleEditText.setText("");

                //clear data in preferences
                utils.resetDiscoverData();
                break;

            case R.id.done:
                //Unselected Discover tab in navigation drawer ( static reference to MovieList isn't best idea)
                MovieListActivity.drawerResult.setSelection(UNSELECTED);

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
                years[i] = String.valueOf(CURRENT_YEAR - i);
        }
    }
}
