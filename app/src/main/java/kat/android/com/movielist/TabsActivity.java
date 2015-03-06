package kat.android.com.movielist;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;


import kat.android.com.movielist.common.PreferencesUtils;
import kat.android.com.movielist.fragments.TabsContainerFragment;
import kat.android.com.movielist.fragments.tabs.SearchFragmentTab;

//Activity which responsible for placing TabsFragments or SearchFragment
public class TabsActivity extends ActionBarActivity implements MenuItemCompat.OnActionExpandListener {

    private PreferencesUtils utils;

    private Fragment fragment;
    private FragmentManager fm;

    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);

        utils = PreferencesUtils.get(getApplicationContext());
        fm = getSupportFragmentManager();
        fragment = fm.findFragmentById(R.id.activity_tabs);

        if (fragment == null) {
            fragment = new TabsContainerFragment();
            fm.beginTransaction()
                    .add(R.id.activity_tabs, fragment)
                    .commit();
        }
    }

    //
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tabs, menu);

        //menu item text change depending on session user
        if (utils.isGuest())
            menu.getItem(1).setTitle("login");
        else
            menu.getItem(1).setTitle("logout");


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

        if (item.getItemId() == R.id.search) {
            //if search menu item is clicked , SearchFragment replace TabFragment
            fm.beginTransaction().replace(R.id.activity_tabs, new SearchFragmentTab()).commit();
            return true;

        } else if (item.getItemId() == R.id.logInOut) {
            //Log In/Out logic
            if (utils.isGuest()) {
                utils.setGuest(false);
                utils.logoutGuestSessionUser();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            } else {
                utils.setGuest(true);
                utils.logoutSessionUser();
                startActivity(new Intent(getApplicationContext(), TabsActivity.class));
            }
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
        fm.beginTransaction().replace(R.id.activity_tabs, new TabsContainerFragment()).commit();
        return true;
    }
}

