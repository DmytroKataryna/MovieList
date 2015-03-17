package kat.android.com.movielist;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;


import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import kat.android.com.movielist.common.PreferencesUtils;

//Activity which responsible for placing TabsFragments or SearchFragment
public class MovieListActivity extends ActionBarActivity implements MenuItemCompat.OnActionExpandListener {

    private PreferencesUtils utils;

    private static final int POPULAR_FRAGMENT = 0;
    private static final int UPCOMING_FRAGMENT = 1;
    private static final int TOP_RATED_FRAGMENT = 2;
    private static final int SEARCH_FRAGMENT = 3;
    private static final int DISCOVER_FRAGMENT = 4;
    private static final int FAVORITE_FRAGMENT = 5;
    private static final int WATCHLIST_FRAGMENT = 6;
    private static final int LOG_IN_OUT = 7;

    private static final int FRAGMENT_COUNT = WATCHLIST_FRAGMENT + 1;
    private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];

    public static Drawer.Result drawerResult;

    private FragmentManager fm;
    protected SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Popular");
        utils = PreferencesUtils.get(getApplicationContext());

        fm = getSupportFragmentManager();
        fragments[POPULAR_FRAGMENT] = fm.findFragmentById(R.id.fragment_popular);
        fragments[UPCOMING_FRAGMENT] = fm.findFragmentById(R.id.fragment_upcoming);
        fragments[TOP_RATED_FRAGMENT] = fm.findFragmentById(R.id.fragment_toprated);
        fragments[SEARCH_FRAGMENT] = fm.findFragmentById(R.id.fragment_search);
        fragments[DISCOVER_FRAGMENT] = fm.findFragmentById(R.id.fragment_discover);
        fragments[FAVORITE_FRAGMENT] = fm.findFragmentById(R.id.fragment_favorite);
        fragments[WATCHLIST_FRAGMENT] = fm.findFragmentById(R.id.fragment_watchlist);

        //hide all fragments
        hideFragment();

        drawerResult = new Drawer()
                .withActivity(this)
                .withActionBarDrawerToggle(true)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_popular).withIcon(FontAwesome.Icon.faw_film).withIdentifier(0),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_upcoming).withIcon(FontAwesome.Icon.faw_eye).withIdentifier(1),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_top_rated).withIcon(FontAwesome.Icon.faw_star).withIdentifier(2),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_discover).withIcon(FontAwesome.Icon.faw_search).withIdentifier(4),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_favorites).withIcon(FontAwesome.Icon.faw_heart),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_watch_list).withIcon(FontAwesome.Icon.faw_list_alt),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_login).withIcon(FontAwesome.Icon.faw_sign_in)
                ).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long index, IDrawerItem iDrawerItem) {
                        switch (position) {
                            case POPULAR_FRAGMENT:
                                showFragment(POPULAR_FRAGMENT);
                                break;
                            case UPCOMING_FRAGMENT:
                                showFragment(UPCOMING_FRAGMENT);
                                break;
                            case TOP_RATED_FRAGMENT:
                                showFragment(TOP_RATED_FRAGMENT);
                                break;
                            case DISCOVER_FRAGMENT:
                                showFragment(DISCOVER_FRAGMENT);
                                break;
                            case FAVORITE_FRAGMENT + 1:
                                showFragment(FAVORITE_FRAGMENT);
                                break;
                            case WATCHLIST_FRAGMENT + 1:
                                showFragment(WATCHLIST_FRAGMENT);
                                break;
                            //log in out button
                            case LOG_IN_OUT + 2:
                                if (utils.isGuest()) {
                                    //login
                                    utils.logoutGuestSessionUser();
                                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                } else {
                                    //logout
                                    utils.logoutSessionUser();
                                    startActivity(new Intent(getApplicationContext(), MovieListActivity.class)
                                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                }
                                break;
                        }
                    }
                }).withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public void onDrawerOpened(View view) {
                        if (utils.isGuest()) {
                            drawerResult.updateItem(new SecondaryDrawerItem().withName(R.string.drawer_item_favorites).withIcon(FontAwesome.Icon.faw_heart).setEnabled(false), 6);
                            drawerResult.updateItem(new SecondaryDrawerItem().withName(R.string.drawer_item_watch_list).withIcon(FontAwesome.Icon.faw_list_alt).setEnabled(false), 7);
                            drawerResult.updateItem(new SecondaryDrawerItem().withName(R.string.drawer_item_login).withIcon(FontAwesome.Icon.faw_sign_in), 9);
                        } else {
                            drawerResult.updateItem(new SecondaryDrawerItem().withName(R.string.drawer_item_favorites).withIcon(FontAwesome.Icon.faw_heart).setEnabled(true), 6);
                            drawerResult.updateItem(new SecondaryDrawerItem().withName(R.string.drawer_item_watch_list).withIcon(FontAwesome.Icon.faw_list_alt).setEnabled(true), 7);
                            drawerResult.updateItem(new SecondaryDrawerItem().withName(R.string.drawer_item_logout).withIcon(FontAwesome.Icon.faw_sign_out), 9);
                        }
                    }

                    @Override
                    public void onDrawerClosed(View view) {

                    }
                }).withSelectedItem(POPULAR_FRAGMENT).build();
    }

    private void hideFragment() {
        FragmentTransaction transaction = fm.beginTransaction();
        for (int i = 1; i < fragments.length; i++) {
            transaction.hide(fragments[i]);
        }
        transaction.commit();
    }

    protected void showFragment(int fragmentIndex) {
        FragmentTransaction transaction = fm.beginTransaction();
        for (int i = 0; i < fragments.length; i++) {
            if (i == fragmentIndex) {
                transaction.show(fragments[i]);
            } else transaction.hide(fragments[i]);
        }
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tabs, menu);

        //configuring search View
        MenuItem searchItem = menu.findItem(R.id.search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        View searchPlate = searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
        searchPlate.setBackgroundResource(R.drawable.appcompat_textfield_activated_holo_dark);

        MenuItem menuItem = menu.findItem(R.id.search);
        if (menuItem != null) {
            MenuItemCompat.setOnActionExpandListener(menuItem, this);
            MenuItemCompat.setActionView(menuItem, searchView);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            //if search menu item is clicked , show SearchFragment
            case R.id.search:
                if (drawerResult.isDrawerOpen())
                    drawerResult.closeDrawer();
                showFragment(SEARCH_FRAGMENT);
                return true;

            case android.R.id.home:
                //home button listener (close/open navigation drawer)
                if (drawerResult.isDrawerOpen())
                    drawerResult.closeDrawer();
                else
                    drawerResult.openDrawer();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return true;
    }

    //When search menu item closed , TabsFragment replace SearchFragment
    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        drawerResult.setSelection(TOP_RATED_FRAGMENT);
        showFragment(TOP_RATED_FRAGMENT);
        return true;
    }
}

