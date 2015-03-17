package kat.android.com.movielist.rest;


import kat.android.com.movielist.rest.pojo.movie.MovieResponse;
import kat.android.com.movielist.rest.pojo.moviedetails.MovieDetails;
import kat.android.com.movielist.rest.pojo.person.PersonResult;
import kat.android.com.movielist.rest.pojo.userdatails.Account;
import kat.android.com.movielist.rest.pojo.userdatails.GuestSession;
import kat.android.com.movielist.rest.pojo.userdatails.Session;
import kat.android.com.movielist.rest.pojo.userdatails.Token;
import kat.android.com.movielist.rest.pojo.userdatails.accountstate.AccountState;
import kat.android.com.movielist.rest.pojo.userdatails.accountstate.AccountStateWithoutRate;
import kat.android.com.movielist.rest.pojo.userdatails.post.Favorite;
import kat.android.com.movielist.rest.pojo.userdatails.post.Rating;
import kat.android.com.movielist.rest.pojo.userdatails.post.Status;
import kat.android.com.movielist.rest.pojo.userdatails.post.WatchList;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;


//All Http requests
public interface API {

    public static final String API_KEY = "?api_key=f82b824321e6aa9a03e8f4d037f40955";

    //HTTP GET Json from http://api.themoviedb.org/3/movie/popular?api_key=f82b824321e6aa9a03e8f4d037f40955
    @GET("/movie/popular" + API_KEY)
    void getPopularMovies(@Query("page") int page,
                          Callback<MovieResponse> callback);

    @GET("/movie/upcoming" + API_KEY)
    void getUpcomingMovies(@Query("page") int page,
                           Callback<MovieResponse> callback);

    @GET("/movie/top_rated" + API_KEY)
    void getTopRatedMovies(@Query("page") int page,
                           Callback<MovieResponse> callback);

    //SEARCH GET  http://api.themoviedb.org/3/search/movie?api_key=XXXXXX&query=MOVIE_NAME
    @GET("/search/movie" + API_KEY)
    void searchMovies(@Query("query") String title,
                      Callback<MovieResponse> callback);

    //get movie by id
    @GET("/movie/{id}" + API_KEY)
    void getMovie(@Path("id") int id,
                  Callback<MovieDetails> callback);

    //token
    @GET("/authentication/token/new" + API_KEY)
    void getToken(Callback<Token> callback);

    //login (receive token)
    @GET("/authentication/token/validate_with_login" + API_KEY)
    void getAuthentication(@Query("request_token") String request_token,
                           @Query("username") String username,
                           @Query("password") String password,
                           Callback<Token> callback);

    //receive session id
    @GET("/authentication/session/new" + API_KEY)
    void getSession(@Query("request_token") String request_token,
                    Callback<Session> callback);

    //get account information
    @GET("/account" + API_KEY)
    void getAccount(@Query("session_id") String session,
                    Callback<Account> callback);


    //Check if current film is added to favorite/watchlist and movie rating
    @GET("/movie/{id}/account_states" + API_KEY)
    void getAccountStates(@Path("id") int movie_id,
                          @Query("session_id") String session,
                          Callback<AccountState> callback);

    //Check if current film is added to favorite/watchlist
    @GET("/movie/{id}/account_states" + API_KEY)
    void getAccountStatesWithoutRate(@Path("id") int movie_id,
                                     @Query("session_id") String session,
                                     Callback<AccountStateWithoutRate> callback);

    //get all favorite movies
    @GET("/account/{id}/favorite/movies" + API_KEY)
    void getFavoritesMovies(@Path("id") int id,
                            @Query("session_id") String session,
                            @Query("page") int page,
                            Callback<MovieResponse> callback);

    //get all watchlist movies
    @GET("/account/{id}/watchlist/movies" + API_KEY)
    void getWatchListMovies(@Path("id") int id,
                            @Query("session_id") String session,
                            @Query("page") int page,
                            Callback<MovieResponse> callback);

    //add/remove from favorite
    @POST("/account/{id}/favorite" + API_KEY)
    void addMovieToFavorites(@Path("id") int id,
                             @Query("session_id") String session,
                             @Body Favorite favorite,
                             Callback<Status> callback);

    //add/remove from watchlist
    @POST("/account/{id}/watchlist" + API_KEY)
    void addMovieToWatchList(@Path("id") int id,
                             @Query("session_id") String session,
                             @Body WatchList watchList,
                             Callback<Status> callback);

    //user rate the movie
    @POST("/movie/{id}/rating" + API_KEY)
    void addMovieRating(@Path("id") int id,
                        @Query("session_id") String session,
                        @Body Rating rating,
                        Callback<Status> callback);

    @GET("/authentication/guest_session/new" + API_KEY)
    void getGuestSession(Callback<GuestSession> callback);

    //better use QueryMap
    @GET("/discover/movie" + API_KEY)
    void getDiscoverMovies(@Query("include_adult") boolean adult,
                           @Query("page") Integer page,
                           @Query("primary_release_year") String year,
                           @Query("primary_release_date.gte") String yearGTE,
                           @Query("primary_release_date.lte") String yearLTE,
                           @Query("sort_by") String sort,
                           @Query("vote_average.gte") Float voteGTE,
                           @Query("vote_average.lte") Float voteLTE,
                           @Query("with_genres") Integer genreId,
                           @Query("with_people") String people,
                           Callback<MovieResponse> callback);

    //search person
    @GET("/search/person" + API_KEY)
    void getPerson(@Query("query") String personName,
                   Callback<PersonResult> callback);

    //popular persons
    @GET("/person/popular" + API_KEY)
    void getPopularPerson(@Query("page") int page,
                          Callback<PersonResult> callback);


}