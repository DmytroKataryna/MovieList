package kat.android.com.movielist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import kat.android.com.movielist.common.PreferencesUtils;
import kat.android.com.movielist.rest.RestClient;
import kat.android.com.movielist.rest.pojo.userdatails.Account;
import kat.android.com.movielist.rest.pojo.userdatails.Session;
import kat.android.com.movielist.rest.pojo.userdatails.Token;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class LoginActivity extends Activity {

    private EditText mLoginEditText, mPassEditText;
    private Button mSend;
    private String mLogin, mPassword;
    private String mToken;
    private String mSession_ID;
    private PreferencesUtils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        //utils class which stores user data (login , session , name)
        utils = PreferencesUtils.get(getApplicationContext());

        mLoginEditText = (EditText) findViewById(R.id.loginText);
        mPassEditText = (EditText) findViewById(R.id.passText);
        mSend = (Button) findViewById(R.id.loginButton);

        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLogin = mLoginEditText.getText().toString();
                mPassword = mPassEditText.getText().toString();
                //start a chain requests
                getLogin();
            }
        });

    }

    //token
    private void getLogin() {
        RestClient.get().getToken(new Callback<Token>() {
            @Override
            public void success(Token token, Response response) {
                mToken = token.getRequest_token();
                userAuthentication();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), "Failed to get token", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //authentication
    private void userAuthentication() {
        RestClient.get().getAuthentication(mToken, mLogin, mPassword, new Callback<Token>() {
            @Override
            public void success(Token token, Response response) {
                mToken = token.getRequest_token();
                getSession();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), "auth failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //session
    private void getSession() {
        RestClient.get().getSession(mToken, new Callback<Session>() {
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
                startActivity(new Intent(getApplicationContext(), TabsActivity.class));
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), "Account failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
