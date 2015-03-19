package kat.android.com.movielist;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;
import kat.android.com.movielist.common.PreferencesUtils;
import kat.android.com.movielist.rest.RestClient;
import kat.android.com.movielist.rest.pojo.userdatails.Account;
import kat.android.com.movielist.rest.pojo.userdatails.GuestSession;
import kat.android.com.movielist.rest.pojo.userdatails.Session;
import kat.android.com.movielist.rest.pojo.userdatails.Token;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class LoginActivity extends Activity implements View.OnClickListener {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "LolVJEUn64nTAR7fGbvEXVKR6";
    private static final String TWITTER_SECRET = "IKFbvHzh3K7q9koqYzkHONuq9dVSoTrtKVLN8CbCuHrRurfTUa";

    private EditText mLoginEditText, mPassEditText;
    private Button mSend, mReg;
    private String mLogin, mPassword;
    private String mToken;
    private String mSession_ID;
    private PreferencesUtils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.login_layout);

        //utils class which stores user data (login , session , name)
        utils = PreferencesUtils.get(getApplicationContext());

        mLoginEditText = (EditText) findViewById(R.id.loginText);
        mPassEditText = (EditText) findViewById(R.id.passText);
        mSend = (Button) findViewById(R.id.loginButton);
        mReg = (Button) findViewById(R.id.regButton);

        mSend.setOnClickListener(this);
        mReg.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkSession();
    }

    private void checkSession() {

        if (utils.isUserLogin()) {
            //if user session is already exist , then just start MovieList activity
            if (utils.getSessionID() != null) {
                utils.setGuest(false);
                startActivity(new Intent(getApplicationContext(), MovieListActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        } else {
            //if it is first launch of the program , then create guest session id
            if (utils.getGuestSessionID() == null) {
                getGuestLogin();
                startActivity(new Intent(getApplicationContext(), MovieListActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            } else {
                //if it isn't first launch of the program (so guest session already exist)
                utils.setGuest(true);
                startActivity(new Intent(getApplicationContext(), MovieListActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.loginButton:
                mLogin = mLoginEditText.getText().toString();
                mPassword = mPassEditText.getText().toString();
                //start a login chain requests
                getLogin();
                break;

            case R.id.regButton:
                String url = "https://www.themoviedb.org/account/signup";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;
        }
    }
    //******************************************************************************************

    private void getGuestLogin() {
        RestClient.get().getGuestSession(new Callback<GuestSession>() {
            @Override
            public void success(GuestSession guestSession, Response response) {
                utils.storeGuestSessionUser(guestSession.getGuest_session_id());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(DetailActivity.TAG, "Guest Fail");
            }
        });
    }

    //******************************************************************************************


    //token
    private void getLogin() {
        RestClient.get().getToken(new Callback<Token>() {
            @Override
            public void success(Token token, Response response) {
                //mToken = token.getRequest_token();
                userAuthentication(token.getRequest_token());
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), "Failed to get token", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //authentication
    private void userAuthentication(String token) {
        RestClient.get().getAuthentication(token, mLogin, mPassword, new Callback<Token>() {
            @Override
            public void success(Token token, Response response) {
                getSession(token.getRequest_token());
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), "please enter correct credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //session
    private void getSession(String token) {
        RestClient.get().getSession(token, new Callback<Session>() {
            @Override
            public void success(Session session, Response response) {
                mSession_ID = session.getSession_id();
                getAccount();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), "Session FAIL", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //account parameters
    private void getAccount() {
        RestClient.get().getAccount(mSession_ID, new Callback<Account>() {
            @Override
            public void success(Account account, Response response) {
                utils.storeSessionUser(account.getId(), account.getUsername(), mSession_ID);

                startActivity(new Intent(getApplicationContext(), MovieListActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), "Account failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //if user change mind and doesn't want login
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        utils.setGuest(true);
        utils.setUserLogin(false);
    }
}
