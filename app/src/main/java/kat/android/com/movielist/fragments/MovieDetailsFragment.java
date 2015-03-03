package kat.android.com.movielist.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import kat.android.com.movielist.DetailActivity;
import kat.android.com.movielist.R;
import kat.android.com.movielist.common.PreferencesUtils;
import kat.android.com.movielist.rest.RestClient;
import kat.android.com.movielist.rest.pojo.userdatails.accountstate.AccountStateWithoutRate;
import kat.android.com.movielist.rest.pojo.userdatails.post.Favorite;
import kat.android.com.movielist.rest.pojo.userdatails.accountstate.AccountState;
import kat.android.com.movielist.rest.pojo.userdatails.post.Rating;
import kat.android.com.movielist.rest.pojo.userdatails.post.Status;
import kat.android.com.movielist.rest.pojo.moviedetails.MovieDetails;
import kat.android.com.movielist.rest.pojo.userdatails.post.WatchList;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

//Detailed profile information
public class MovieDetailsFragment extends Fragment implements View.OnClickListener, RatingBar.OnRatingBarChangeListener {


    private int id;
    private boolean favorite;
    private boolean watchList;
    private float rating;
    private ImageView mImage;
    private MovieDetails data;
    private TextView mTitle, mGenres, mRelease, mRuntime, mBudget;
    private TextView mAvgRate, mCount, mDescription, mHomePage;
    private PreferencesUtils utils;
    private Button mFavoriteButt, mWatchListButt;
    private RatingBar mRatingBar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get user Id from bundle
        id = getArguments().getInt(DetailActivity.ID_KEY);
        //utils class which stores user data (login , session , name)
        utils = PreferencesUtils.get(getActivity());
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

        mFavoriteButt = (Button) v.findViewById(R.id.addFav);
        mFavoriteButt.setOnClickListener(this);

        mWatchListButt = (Button) v.findViewById(R.id.addWatch);
        mWatchListButt.setOnClickListener(this);

        mRatingBar = (RatingBar) v.findViewById(R.id.ratingBar);
        mRatingBar.setOnRatingBarChangeListener(this);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //get detail info about current movie
        loadMovieInformation();
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

                loadMovieAccountStateInformation();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(DetailActivity.TAG, "An error occurred while downloading movie info.");
            }
        });
    }

    //check if current movie belongs to favorite list
    private void loadMovieAccountStateInformation() {
        RestClient.get().getAccountStates(id, utils.getSessionID(), new Callback<AccountState>() {

            @Override
            public void success(AccountState accountState, Response response) {
                favorite = accountState.isFavorite();
                watchList = accountState.isWatchlist();
                rating = accountState.getRated().getValue();
                mRatingBar.setRating(rating);
                //int rate = also get rating and set value to ratingBar
                if (favorite)
                    mFavoriteButt.setBackgroundResource(R.drawable.ic_action_favorite_orange);
                else
                    mFavoriteButt.setBackgroundResource(R.drawable.ic_action_favorite_gray);
                if (watchList)
                    mWatchListButt.setBackgroundResource(R.drawable.ic_action_watchlist_orange);
                else
                    mWatchListButt.setBackgroundResource(R.drawable.ic_action_watchlist_gray);
            }

            @Override
            public void failure(RetrofitError error) {
                //if movie isn't rated, then load just favorite and watchlist info
                loadMovieAccountStateInformationWithoutRating();

                Log.d(DetailActivity.TAG, "Account States failed");
            }
        });
    }

    //check if current movie belongs to favorite list
    private void loadMovieAccountStateInformationWithoutRating() {
        RestClient.get().getAccountStatesWithoutRate(id, utils.getSessionID(), new Callback<AccountStateWithoutRate>() {

            @Override
            public void success(AccountStateWithoutRate accountState, Response response) {
                favorite = accountState.isFavorite();
                watchList = accountState.isWatchlist();
                if (favorite)
                    mFavoriteButt.setBackgroundResource(R.drawable.ic_action_favorite_orange);
                else
                    mFavoriteButt.setBackgroundResource(R.drawable.ic_action_favorite_gray);
                if (watchList)
                    mWatchListButt.setBackgroundResource(R.drawable.ic_action_watchlist_orange);
                else
                    mWatchListButt.setBackgroundResource(R.drawable.ic_action_watchlist_gray);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(DetailActivity.TAG, "Account States failed");
            }
        });
    }

    //change movie favorite state
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

    //change movie watchlist state
    private void movieWatchListChange(boolean state) {
        RestClient.get().addMovieToWatchList(utils.getSessionUserID(), utils.getSessionID(), new WatchList("movie", id, state), new Callback<Status>() {
            @Override
            public void success(Status status, Response response) {
                Log.d(DetailActivity.TAG, "WatchList " + status.getStatus_message());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(DetailActivity.TAG, "An error occurred while adding movie to  watchList.");
            }
        });
    }

    //change movie rating state
    private void movieRatingChange(float rating) {
        RestClient.get().addMovieRating(id, utils.getSessionID(), new Rating(rating), new Callback<Status>() {
            @Override
            public void success(Status status, Response response) {
                Log.d(DetailActivity.TAG, "Rating Changed");
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(DetailActivity.TAG, "Rating changing failed");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //favorite button
            case R.id.addFav:
                if (favorite) {
                    movieFavoritesChange(false);
                    mFavoriteButt.setBackgroundResource(R.drawable.ic_action_favorite_gray);
                    favorite = !favorite;
                } else {
                    movieFavoritesChange(true);
                    mFavoriteButt.setBackgroundResource(R.drawable.ic_action_favorite_orange);
                    favorite = !favorite;
                }
                break;
            //watchlist button
            case R.id.addWatch:
                if (watchList) {
                    movieWatchListChange(false);
                    mWatchListButt.setBackgroundResource(R.drawable.ic_action_watchlist_gray);
                    watchList = !watchList;
                } else {
                    movieWatchListChange(true);
                    mWatchListButt.setBackgroundResource(R.drawable.ic_action_watchlist_orange);
                    watchList = !watchList;
                }
                break;
        }
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        movieRatingChange(rating);
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

