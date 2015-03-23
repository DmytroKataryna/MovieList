package kat.android.com.movielist.rest;

import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

//Creating RestAdapter by using Retrofit
//apply singleton pattern
public class RestClient {
    private static API REST_CLIENT;
    private static final String ROOT =
            "http://api.themoviedb.org/3";

    public static final String IMAGE_ROOT =
            "https://image.tmdb.org/t/p";


    static {
        setupRestClient();
    }

    private RestClient() {
    }

    public static API get() {
        return REST_CLIENT;
    }

    private static void setupRestClient() {
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(ROOT)
                .setClient(new OkClient(new OkHttpClient()))
                .setLogLevel(RestAdapter.LogLevel.FULL);

        RestAdapter restAdapter = builder.build();
        REST_CLIENT = restAdapter.create(API.class);
    }
}
