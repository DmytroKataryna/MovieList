package kat.android.com.movielist.rest;


import kat.android.com.movielist.rest.pojo.Favorite;
import kat.android.com.movielist.rest.pojo.userdatails.Account;
import kat.android.com.movielist.rest.pojo.userdatails.AccountState;
import kat.android.com.movielist.rest.pojo.userdatails.Session;
import kat.android.com.movielist.rest.pojo.userdatails.Status;
import kat.android.com.movielist.rest.pojo.userdatails.Token;
import kat.android.com.movielist.rest.pojo.moviedetails.MovieDetails;
import kat.android.com.movielist.rest.pojo.MovieResponse;
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

    @GET("/authentication/token/new" + API_KEY)
    void getToken(Callback<Token> callback);

    //https://api.themoviedb.org/3/authentication/token/validate_with_login?api_key=API_KEY&request_token=REQUEST_TOKEN&username=###&password=###
    @GET("/authentication/token/validate_with_login" + API_KEY)
    void getAuthentication(@Query("request_token") String request_token,
                           @Query("username") String username,
                           @Query("password") String password,
                           Callback<Token> callback);

    @GET("/authentication/session/new" + API_KEY)
    void getSession(@Query("request_token") String request_token,
                    Callback<Session> callback);

    @GET("/account" + API_KEY)
    void getAccount(@Query("session_id") String session,
                    Callback<Account> callback);

    //add/remove from favorite
//    @POST("/account/{id}/favorite" + API_KEY)
//    void addMovieToFavorites(@Path("id") int id,
//                             @Query("session_id") String session,
//                             @QueryMap Map<String, Object> parameters,
//                             Callback<Status> callback);

//    //add/remove from favorite
//    @POST("/account/{id}/favorite" + API_KEY)
//    void addMovieToFavorites(@Path("id") int id,
//                             @Query("session_id") String session,
//                             @Field("media_type") String media_type,
//                             @Field("media_id") Integer media_id,
//                             @Field("favorite") Boolean favorite,
//                             Callback<Status> callback);

    //add/remove from favorite
    @POST("/account/{id}/favorite" + API_KEY)
    void addMovieToFavorites(@Path("id") int id,
                             @Query("session_id") String session,
                             @Body Favorite favorite,
                             Callback<Status> callback);

    //get all favorite movies
    @GET("/account/{id}/favorite/movies" + API_KEY)
    void getFavoritesMovies(@Path("id") int id,
                            @Query("session_id") String session,
                            @Query("page") int page,
                            Callback<MovieResponse> callback);

    //Check if current film is added to favorite list
    @GET("/movie/{id}/account_states" + API_KEY)
    void getAccount_states(@Path("id") int movie_id,
                           @Query("session_id") String session,
                           Callback<AccountState> callback);
}
