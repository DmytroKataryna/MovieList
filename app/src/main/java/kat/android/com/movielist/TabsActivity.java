package kat.android.com.movielist;


import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import kat.android.com.movielist.common.TabListener;
import kat.android.com.movielist.fragments.tabs.FavoritesFragmentTab;
import kat.android.com.movielist.fragments.tabs.PopularFragmentTab;
import kat.android.com.movielist.fragments.tabs.SearchFragmentTab;
import kat.android.com.movielist.fragments.tabs.TopRatedFragmentTab;
import kat.android.com.movielist.fragments.tabs.UpcomingFragmentTab;


public class TabsActivity extends ActionBarActivity {

    ActionBar.Tab popularTab, upcomingTab, topRatedTab, searchTab, favoritesTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);

        // Screen handling while hiding Actionbar title.
        actionBar.setDisplayShowTitleEnabled(false);

        // Creating ActionBar tabs.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        popularTab = actionBar.newTab()
                .setText("Popular")
                .setTabListener(new TabListener(PopularFragmentTab.class, getApplicationContext()));
        upcomingTab = actionBar.newTab()
                .setText("Upcoming")
                .setTabListener(new TabListener(UpcomingFragmentTab.class, getApplicationContext()));
        topRatedTab = actionBar.newTab()
                .setText("Top Rated")
                .setTabListener(new TabListener(TopRatedFragmentTab.class, getApplicationContext()));
        searchTab = actionBar.newTab()
                .setText("Search")
                .setTabListener(new TabListener(SearchFragmentTab.class, getApplicationContext()));

        favoritesTab = actionBar.newTab()
                .setText("Favorites")
                .setTabListener(new TabListener(FavoritesFragmentTab.class, getApplicationContext()));

        actionBar.addTab(popularTab);
        actionBar.addTab(upcomingTab);
        actionBar.addTab(topRatedTab);
        actionBar.addTab(favoritesTab);
        actionBar.addTab(searchTab);
    }
}
