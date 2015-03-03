package kat.android.com.movielist.common;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;

//  tabs callbacks , here implemented tabs logic ( selected , unselected)
public class TabListener implements ActionBar.TabListener {

    private Fragment mFragment;
    private Class mClass;
    private Context context;

    public TabListener(Class mClass, Context context) {
        this.mClass = mClass;
        this.context = context;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // replacing fragments between each other
        if (mFragment == null) {
            mFragment = Fragment.instantiate(context, mClass.getName());
            fragmentTransaction.replace(android.R.id.content, mFragment);
        } else {
            fragmentTransaction.attach(mFragment);
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // when a fragment is no longer needed, detach it from the activity but dont destroy it
        if (mFragment != null)
            fragmentTransaction.detach(mFragment);


    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }
}
