package kat.android.com.movielist.fragments.tabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import kat.android.com.movielist.R;
import kat.android.com.movielist.common.PreferencesUtils;

public class DiscoverFragmentTab extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private PreferencesUtils utils;
    private Spinner mYearSpinner, mSortSpinner, mYearOrderSpinner, mRatingSpinner, mRatingOrderSpinner;
    private Button sendSearch;
    private String years[] = new String[99];
    private static final int CURRENT_YEAR = 2015;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        utils = PreferencesUtils.get(getActivity());

        //init years
        initYearArray();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.discover_search_layout, container, false);

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
        sendSearch = (Button) v.findViewById(R.id.sendSearch);
        sendSearch.setOnClickListener(this);

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
        if (v.getId() == R.id.sendSearch) {
            getFragmentManager().beginTransaction().hide(getFragmentManager().findFragmentById(R.id.fragment_discover)).commit();
            getFragmentManager().beginTransaction().replace(R.id.fragment_discover_movies_list, new DiscoverResultFragmentTab()).commit();
        }
        if (v.getId() == R.id.resetButton)
            utils.resetDiscoverData();
    }

    private void initYearArray() {
        for (int i = 0; i < years.length; i++) {
            if (i == 0)
                years[i] = "None";
            else
                years[i] = String.valueOf(CURRENT_YEAR - i);
        }
    }


    //here i need save data to preferences
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.yearSpinner:
                Log.d("SPINNER", "YEAR " + parent.getItemAtPosition(position).toString());
                utils.setReleaseYear(parent.getItemAtPosition(position).toString());
                break;
            case R.id.yearOrderSpinner:
                Log.d("SPINNER", "YEAR ORDER " + parent.getItemAtPosition(position).toString());
                utils.setReleaseOrder(parent.getItemAtPosition(position).toString());
                break;
            case R.id.sortBySpinner:
                Log.d("SPINNER", "SORT ORDER " + parent.getItemAtPosition(position).toString());
                utils.setSortOrder(parent.getItemAtPosition(position).toString());
                break;
            case R.id.ratingSpinner:
                Log.d("SPINNER", "Rating " + parent.getItemAtPosition(position).toString());
                utils.setVoteAvg(parent.getItemAtPosition(position).toString());
                break;
            case R.id.ratingOrderSpinner:
                Log.d("SPINNER", "Rating Order " + parent.getItemAtPosition(position).toString());
                utils.setVoteOrder(parent.getItemAtPosition(position).toString());
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
