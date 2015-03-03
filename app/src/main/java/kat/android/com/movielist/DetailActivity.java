package kat.android.com.movielist;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

import kat.android.com.movielist.fragments.MovieDetailsFragment;

//Activity which displays information about current movie
public class DetailActivity extends Activity {

    public static final String TAG = "movie.list.log";
    public static final String ID_KEY = "IDD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //receiving movie id from intent , and send it to fragment ( by adding it to bundle)
        Bundle bundle = new Bundle();
        bundle.putInt(ID_KEY, getIntent().getIntExtra("KEY", 0));

        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.activity_main);

        if (fragment == null) {
            fragment = new MovieDetailsFragment();
            fragment.setArguments(bundle);
            fm.beginTransaction().add(R.id.activity_main, fragment).commit();
        }
    }
}
