package kat.android.com.movielist.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.squareup.picasso.Picasso;

import org.lucasr.twowayview.TwoWayView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kat.android.com.movielist.DetailActivity;
import kat.android.com.movielist.R;
import kat.android.com.movielist.common.CastAdapter;
import kat.android.com.movielist.common.DeveloperKey;
import kat.android.com.movielist.common.ImageAdapter;
import kat.android.com.movielist.common.PreferencesUtils;
import kat.android.com.movielist.rest.RestClient;
import kat.android.com.movielist.rest.pojo.images.Backdrop;
import kat.android.com.movielist.rest.pojo.images.Cast;
import kat.android.com.movielist.rest.pojo.images.Credits;
import kat.android.com.movielist.rest.pojo.images.Image;
import kat.android.com.movielist.rest.pojo.moviedetails.MovieDetails;
import kat.android.com.movielist.rest.pojo.moviedetails.Videos;
import kat.android.com.movielist.rest.pojo.userdatails.accountstate.AccountState;
import kat.android.com.movielist.rest.pojo.userdatails.accountstate.AccountStateWithoutRate;
import kat.android.com.movielist.rest.pojo.userdatails.post.Favorite;
import kat.android.com.movielist.rest.pojo.userdatails.post.Rating;
import kat.android.com.movielist.rest.pojo.userdatails.post.Status;
import kat.android.com.movielist.rest.pojo.userdatails.post.WatchList;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

//Detailed profile information
public class MovieDetailsFragment extends Fragment implements View.OnClickListener, RatingBar.OnRatingBarChangeListener {


    private int id;
    private boolean favorite;
    private boolean watchList;
    private float rating;
    private String movieTitle;
    private String trailerKey;
    private ImageView mImage;
    private MovieDetails data;
    private TextView mTitle, mGenres, mRelease, mRuntime, mBudget;
    private TextView mAvgRate, mCount, mDescription, mHomePage, mCastText;
    private PreferencesUtils utils;
    private Button mFavoriteButt, mWatchListButt, mYouTubeButton, mFaceBookButton, mTwitterButton;
    private RatingBar mRatingBar;
    private List<Backdrop> images = new ArrayList<>();
    private List<Cast> cast = new ArrayList<>();
    private TwoWayView imagesListView, castListView;
    private BaseAdapter imagesAdapter, castAdapter;

    private UiLifecycleHelper uiHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get user Id from bundle
        id = getArguments().getInt(DetailActivity.ID_KEY);
        //utils class which stores user data (login , session , name)
        utils = PreferencesUtils.get(getActivity());

        uiHelper = new UiLifecycleHelper(getActivity(), null);
        uiHelper.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.details_movie_layout, container, false);

        //**********fix bug ( when user open movie detail , scroll view isn't displayed at the top of the screen)
        final ScrollView scroll = (ScrollView) v.findViewById(R.id.detailsScrollView);
        scroll.post(new Runnable() {
            @Override
            public void run() {
                scroll.fullScroll(View.FOCUS_UP);
                scroll.smoothScrollTo(scroll.getTop(), scroll.getTop());
            }
        });
        //****************************************************************************************

        imagesAdapter = new ImageAdapter(getActivity(), images);
        castAdapter = new CastAdapter(getActivity(), cast);

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
        mCastText = (TextView) v.findViewById(R.id.castTextView);

        mFavoriteButt = (Button) v.findViewById(R.id.addFav);
        mFavoriteButt.setOnClickListener(this);

        mWatchListButt = (Button) v.findViewById(R.id.addWatch);
        mWatchListButt.setOnClickListener(this);

        mYouTubeButton = (Button) v.findViewById(R.id.youTubeButton);
        mYouTubeButton.setBackgroundResource(R.drawable.ic_youtube_gray);
        mYouTubeButton.setOnClickListener(this);
        mYouTubeButton.setEnabled(false);

        mFaceBookButton = (Button) v.findViewById(R.id.faceBookButton);
        mFaceBookButton.setBackgroundResource(R.drawable.ic_facebook_button);
        mFaceBookButton.setOnClickListener(this);

        mTwitterButton = (Button) v.findViewById(R.id.twitterButton);
        mTwitterButton.setBackgroundResource(R.drawable.ic_twitter_circle);
        mTwitterButton.setOnClickListener(this);

        mRatingBar = (RatingBar) v.findViewById(R.id.ratingBar);
        mRatingBar.setOnRatingBarChangeListener(this);

        imagesListView = (TwoWayView) v.findViewById(R.id.lvItems);
        imagesListView.setAdapter(imagesAdapter);

        castListView = (TwoWayView) v.findViewById(R.id.castLvItems);
        castListView.setAdapter(castAdapter);


        if (utils.isGuest()) {
            mFavoriteButt.setVisibility(View.GONE);
            mWatchListButt.setVisibility(View.GONE);
            mRatingBar.setVisibility(View.GONE);
        }

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //get detail info about current movie
        loadMovieInformation();
        //load movie images
        loadMovieImages();
        //load movie cast
        loadMovieCast();
        //load movie videos
        loadMovieVideos();
    }

    //get detail info about current movie
    private void loadMovieInformation() {

        RestClient.get().getMovie(id, new Callback<MovieDetails>() {
            @Override
            public void success(MovieDetails movieDetails, Response response) {
                data = movieDetails;
                movieTitle = movieDetails.getTitle();

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
                //if guest session  ->then  didn't upload favorite/rating/watchlist info
                if (!utils.isGuest())
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

                if (watchList)
                    mWatchListButt.setBackgroundResource(R.drawable.ic_action_watchlist_orange);
                else
                    mWatchListButt.setBackgroundResource(R.drawable.ic_action_watchlist_gray);

                if (favorite)
                    mFavoriteButt.setBackgroundResource(R.drawable.ic_action_favorite_orange);
                else
                    mFavoriteButt.setBackgroundResource(R.drawable.ic_action_favorite_gray);
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

                if (watchList)
                    mWatchListButt.setBackgroundResource(R.drawable.ic_action_watchlist_orange);
                else
                    mWatchListButt.setBackgroundResource(R.drawable.ic_action_watchlist_gray);

                if (favorite)
                    mFavoriteButt.setBackgroundResource(R.drawable.ic_action_favorite_orange);
                else
                    mFavoriteButt.setBackgroundResource(R.drawable.ic_action_favorite_gray);
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

    //load movie images(backdrops)
    private void loadMovieImages() {
        RestClient.get().getMovieImages(id, new Callback<Image>() {
            @Override
            public void success(Image image, Response response) {
                images = image.getBackdrops();
                imagesListView.setAdapter(new ImageAdapter(getActivity(), images));

                //if movie doesn't contain pictures , then set ListView visibility to GONE
                if (images.size() == 0) imagesListView.setVisibility(View.GONE);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(DetailActivity.TAG, "Images loading failed");
            }
        });
    }

    //load movie cast
    private void loadMovieCast() {
        RestClient.get().getMovieCast(id, new Callback<Credits>() {
            @Override
            public void success(Credits credits, Response response) {
                cast = credits.getCast();
                castListView.setAdapter(new CastAdapter(getActivity(), cast));

                //if movie doesn't contain cast , then set ListView /Cast Text visibility to GONE
                if (cast.size() == 0) {
                    castListView.setVisibility(View.GONE);
                    mCastText.setVisibility(View.GONE);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(DetailActivity.TAG, "Cast loading failed");
            }
        });
    }

    //load movie videos
    private void loadMovieVideos() {
        RestClient.get().getMovieVideos(id, new Callback<Videos>() {
            @Override
            public void success(Videos videos, Response response) {
                //get movie trailers , if list isn't empty then change background and make butt enable to click
                if (videos.getResults() != null
                        && videos.getResults().size() > 0
                        && videos.getResults().get(0).getSite().equals("YouTube")) {
                    mYouTubeButton.setEnabled(true);
                    mYouTubeButton.setBackgroundResource(R.drawable.ic_youtube);
                    trailerKey = videos.getResults().get(0).getKey();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(DetailActivity.TAG, "Movie videos data loading failed");
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

            //youTube button
            case R.id.youTubeButton:
                startActivity(
                        YouTubeStandalonePlayer.createVideoIntent(getActivity(), DeveloperKey.DEVELOPER_KEY, trailerKey));
                break;

            case R.id.faceBookButton:
                //     facebook share dialog
                FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(getActivity())
                        .setLink("https://www.themoviedb.org/movie/" + id)
                        .setName(movieTitle)
                        .setDescription("Check this movie !")
                        .build();
                uiHelper.trackPendingDialogCall(shareDialog.present());
                break;

            case R.id.twitterButton:
                try {
                    makeTweet();
                } catch (TwitterException | IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getActivity(), "You shared this movie in twitter", Toast.LENGTH_LONG).show();
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


    //******************************  FACEBOOK  ***************************************
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        uiHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
            @Override
            public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
                Log.e("Activity", String.format("Error: %s", error.toString()));
            }

            @Override
            public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
                Log.i("Activity", "Success!");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    //*************************** Twitter  ***************************

    private void makeTweet() throws TwitterException, IOException {
        new Task().execute();
    }

    class Task extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true)
                    .setOAuthConsumerKey(DeveloperKey.consumer_key)
                    .setOAuthConsumerSecret(DeveloperKey.secret_key)
                    .setOAuthAccessToken(DeveloperKey.access_token)
                    .setOAuthAccessTokenSecret(DeveloperKey.access_token_secret);
            TwitterFactory tf = new TwitterFactory(cb.build());
            Twitter twitter = tf.getInstance();

            try {
                twitter.updateStatus("Just watched " + movieTitle + " movie !");

            } catch (TwitterException e) {
                e.printStackTrace();
            }

            return null;
        }
    }


}

