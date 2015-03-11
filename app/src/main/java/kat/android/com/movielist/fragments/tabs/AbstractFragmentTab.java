package kat.android.com.movielist.fragments.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.quentindommerc.superlistview.OnMoreListener;
import com.quentindommerc.superlistview.SuperListview;

import java.util.ArrayList;
import java.util.List;

import kat.android.com.movielist.DetailActivity;
import kat.android.com.movielist.R;
import kat.android.com.movielist.common.MovieAdapter;
import kat.android.com.movielist.rest.pojo.movie.Movie;


public abstract class AbstractFragmentTab extends Fragment implements AdapterView.OnItemClickListener {

    protected int currentPage = 1;
    protected int totalPages = 1;

    List<Movie> movieList = new ArrayList<>();
    SuperListview listView;
    BaseAdapter adapter;
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        loadFirstPage();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.tab_layout, container, false);
            adapter = new MovieAdapter(getActivity(), movieList);

            listView = (SuperListview) view.findViewById(R.id.listView);
            listView.setOnItemClickListener(this);
            listView.setAdapter(adapter);

            listView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    loadFirstPage();
                    listView.hideMoreProgress();
                }
            });
            listView.setupMoreListener(new OnMoreListener() {
                @Override
                public void onMoreAsked(int numberOfItems, int numberBeforeMore, int currentItemPos) {
                    listView.showMoreProgress();
                    if (currentPage < totalPages)
                        loadData(++currentPage);
                    listView.hideMoreProgress();
                }
            }, 5);

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
                .putExtra("KEY", movieList.get(position).getId());
        startActivity(i);
    }

    private void loadFirstPage() {
        movieList.clear();
        currentPage = 1;
        loadData(currentPage);
    }

    public abstract void loadData(int page);
}
