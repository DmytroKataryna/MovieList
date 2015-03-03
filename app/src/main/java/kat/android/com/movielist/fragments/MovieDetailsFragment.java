package kat.android.com.movielist.fragments;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import kat.android.com.movielist.DetailActivity;
import kat.android.com.movielist.R;
import kat.android.com.movielist.common.PreferencesUtils;
import kat.android.com.movielist.rest.RestClient;
import kat.android.com.movielist.rest.pojo.Favorite;
import kat.android.com.movielist.rest.pojo.userdatails.AccountState;
import kat.android.com.movielist.rest.pojo.userdatails.Status;
import kat.android.com.movielist.rest.pojo.moviedetails.MovieDetails;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

//Detailed profile information
public class MovieDetailsFragment extends Fragment implements View.OnClickListener {


    private int id;
    private boolean favorite;
    private ImageView mImage;
    private MovieDetails data;
    private TextView mTitle, mGenres, mRelease, mRuntime, mBudget;
    private TextView mAvgRate, mCount, mDescription, mHomePage;
    private PreferencesUtils utils;
    private Button addButt;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get user Id from bundle
        id = getArguments().getInt(DetailActivity.ID_KEY);
        //utils class which stores user data (login , session , name)
        utils = PreferencesUtils.get(getActivity());
        //get detail info about current movie
        loadMovieInformation();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.details_movie_layout, container, false);

        mImage = (ImageView) v.findViewById(R.id.posterImageView);
        mTitle = (TextView) v.findViewById(R.id.titleTextView);
        mGenres = (TextView) v.findViewById(R.id.genresTxtView);
        mRelease = (TextView) v.findViewById(R.id.releaseTextView);
        mRuntime = (TextView) v.findViewById(R.id.runtimeTextView);
        mBudget = (TextView) v.findViewById(R.id.budgetTextView);
        mAvgRate = (TextView) v.findViewById(R.id.detailsAvgView);
        mCount = (TextView) v.findViewById(R.id.countView);
        mDescription = (TextView) v.findViewById(R.id.detailsDescriptionView);
        mHomePage = (TextView) v.findViewById(R.id.detailsHomePageView);

        addButt = (Button) v.findViewById(R.id.addFav);
        addButt.setOnClickListener(this);

        return v;
    }


    private void loadMovieInformation() {

        RestClient.get().getMovie(id, new Callback<MovieDetails>() {
            @Override
            public void success(MovieDetails movieDetails, Response response) {
                data = movieDetails;
                Picasso.with(getActivity()).load("https://image.tmdb.org/t/p/w185" + data.getPoster_path()).into(mImage);
                mTitle.setText(data.getTitle());
                mGenres.setText(getGenres());
                mRelease.setText(data.getRelease_date());
                mRuntime.setText(data.getRuntime() + " min");
                mBudget.setText(data.getBudget() + "$");
                mAvgRate.setText(data.getVote_average() + "");
                mCount.setText(data.getVote_count() + "");
                mDescription.setText(data.getOverview());
                //Some movies doesn't have homepage , so if it have i add this text ( also coloring some part)
                if (data.getHomepage() != null && data.getHomepage().length() != 0) {
                    mHomePage.setText(Html.fromHtml("<font color=#FB8C00>Homepage :</font>" + (" <br/>") + data.getHomepage()));
                }

                //check if this movies belongs to favorite list and change CheckBox
                loadMovieFavoriteInformation();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(DetailActivity.TAG, "An error occurred while downloading movie info.");
            }
        });
    }

    //check if current movie belongs to favorite list
    private void loadMovieFavoriteInformation() {
        RestClient.get().getAccount_states(id, utils.getSessionID(), new Callback<AccountState>() {

            @Override
            public void success(AccountState accountState, Response response) {
                favorite = accountState.isFavorite();
                if (favorite)
                    addButt.setBackgroundResource(R.drawable.ic_action_favorite_orange);
                else
                    addButt.setBackgroundResource(R.drawable.ic_action_favorite_gray);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(DetailActivity.TAG, "Account States failed");
            }
        });
    }


    //Movie may contains a lot of genres. I get just three of them
    private String getGenres() {
        StringBuilder builder = new StringBuilder();
        int n = data.getGenres().size();
        if (n == 0) return "";
        else if (n > 3) n = 3;
        for (int i = 0; i < n; i++) {
            builder.append(data.getGenres().get(i).getName()).append(" , ");
        }
        builder.deleteCharAt(builder.lastIndexOf(", "));
        return builder.toString();
    }

//    private void movieFavoritesChange(boolean state) {
//        RestClient.get().addMovieToFavorites(utils.getSessionUserID(), utils.getSessionID(), createMap(state), new Callback<Status>() {
//            @Override
//            public void success(Status status, Response response) {
//                Toast.makeText(getActivity(), "Favorites " + status.getStatus_message(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                Log.d(DetailActivity.TAG, "An error occurred while adding movie to  favorites.");
//            }
//        });
//    }


//    private void movieFavoritesChange(boolean state) {
//        RestClient.get().addMovieToFavorites(utils.getSessionUserID(), utils.getSessionID(), "movie", id, state, new Callback<Status>() {
//            @Override
//            public void success(Status status, Response response) {
//                Toast.makeText(getActivity(), "Favorites " + status.getStatus_message(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                Log.d(DetailActivity.TAG, "An error occurred while adding movie to  favorites.");
//            }
//        });
//    }

    private void movieFavoritesChange(boolean state) {
        RestClient.get().addMovieToFavorites(utils.getSessionUserID(), utils.getSessionID(), new Favorite("movie", id, state), new Callback<Status>() {
            @Override
            public void success(Status status, Response response) {
                Log.d(DetailActivity.TAG, "Favorites " + status.getStatus_message());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(DetailActivity.TAG, "An error occurred while adding movie to  favorites.");
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (favorite) {
            movieFavoritesChange(false);
            addButt.setBackgroundResource(R.drawable.ic_action_favorite_gray);
            favorite = !favorite;
        } else {
            movieFavoritesChange(true);
            addButt.setBackgroundResource(R.drawable.ic_action_favorite_orange);
            favorite = !favorite;
        }
    }
}
