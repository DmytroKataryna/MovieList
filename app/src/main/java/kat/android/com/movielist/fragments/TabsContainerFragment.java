package kat.android.com.movielist.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import kat.android.com.movielist.LoginActivity;
import kat.android.com.movielist.R;
import kat.android.com.movielist.TabsActivity;
import kat.android.com.movielist.common.PreferencesUtils;
import kat.android.com.movielist.fragments.tabs.FavoritesFragmentTab;
import kat.android.com.movielist.fragments.tabs.PopularFragmentTab;
import kat.android.com.movielist.fragments.tabs.TopRatedFragmentTab;
import kat.android.com.movielist.fragments.tabs.UpcomingFragmentTab;
import kat.android.com.movielist.fragments.tabs.WatchListFragmentTab;

public class TabsContainerFragment extends Fragment {

    private FragmentTabHost tabHost;
    private PreferencesUtils utils;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);

        utils = PreferencesUtils.get(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        tabHost = new FragmentTabHost(getActivity());
        tabHost.setup(getActivity(), getChildFragmentManager(), R.layout.activity_tabs);

        tabHost.addTab(tabHost.newTabSpec("Tab1").setIndicator("Popular"),
                PopularFragmentTab.class, null);

        tabHost.addTab(tabHost.newTabSpec("Tab2").setIndicator("Upcoming"),
                UpcomingFragmentTab.class, null);

        tabHost.addTab(tabHost.newTabSpec("Tab3").setIndicator("Top"),
                TopRatedFragmentTab.class, null);

        if (!utils.isGuest()) {
            tabHost.addTab(tabHost.newTabSpec("Tab4").setIndicator("Favorites"),
                    FavoritesFragmentTab.class, null);

            tabHost.addTab(tabHost.newTabSpec("Tab5").setIndicator("WatchList"),
                    WatchListFragmentTab.class, null);
        }
        //change tab style
        tabHost.getTabWidget().setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 75));
        tabHost.getTabWidget().setPadding(0, 3, 0, 0);
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.tab_bar_background);
        }
        return tabHost;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logInOut:
                if (utils.isGuest()) {
                    //login
                    utils.logoutGuestSessionUser();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                } else {
                    //logout
                    utils.logoutSessionUser();
                    startActivity(new Intent(getActivity(), TabsActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
