package com.saadm.myanimelist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.saadm.myanimelist.model.AccessToken;
import com.saadm.myanimelist.service.client.MALClient;

import java.security.SecureRandom;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    Button mLogin;
    Button mDemoAccount;
    ProgressBar mProgressBar;
    String mRedirectUri = "myapp://auth";
    String mBaseURL = "https://myanimelist.net/v1/oauth2/";
    String mClientId = "1c332a538ae8d87c3594a1cd0c705426";
    static String mVerifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(mVerifier == null){
            generateCodeVerifier();
        }

        mLogin = findViewById(R.id.button_Login);
        mDemoAccount = findViewById(R.id.button_DemoAccount);
        mProgressBar = findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.INVISIBLE);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticate();
            }
        });

        mDemoAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Using demo account", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Uri uri = getIntent().getData();

        //Make sure that the app was resumed after catching the redirect URL
        if(uri != null && uri.toString().startsWith(mRedirectUri)){
            String code = uri.getQueryParameter("code");
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(mBaseURL)
                    .addConverterFactory(GsonConverterFactory.create());

            Retrofit retrofit = builder.build();
            MALClient client = retrofit.create(MALClient.class);

            //Async tasl therefore we need Call
            Call<AccessToken> accessToken= client.getAccessToken(
                    mClientId,
                    code,
                    mVerifier,
                    "authorization_code"
            );
            mProgressBar.setVisibility(View.VISIBLE);

            //Begin the async task
            accessToken.enqueue(new Callback<AccessToken>() {
                @Override
                public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                    Toast.makeText(LoginActivity.this, "Successfully authenticated", Toast.LENGTH_SHORT).show();
                    AccessToken token = response.body();
                    String accessToken = token.getAccessToken();
                    SharedPreferences.Editor sharedPrefs = getSharedPreferences(getString(R.string.PREFS_KEY)
                            , MODE_PRIVATE)
                            .edit();
                    sharedPrefs.putString("access_token", token.getAccessToken());
                    sharedPrefs.apply();

//                    Log.i("Token", "Token: " + token.getAccessToken());
//                    Log.i("Token", "Expires in: " + token.getExpiresIn());
                    Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                    intent.putExtra("accessToken", accessToken);
                    startActivity(intent);
                }

                @Override
                public void onFailure(Call<AccessToken> call, Throwable t) {
                    Log.i("Token", "Failed to make request ");
                    Toast.makeText(LoginActivity.this, "Did not get token", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private void authenticate() {
        String url = mBaseURL + "authorize";

        Log.i("Token" , "Verifier1 " + mVerifier);
        Uri authUri = Uri.parse(url + "?response_type=code&client_id=" + mClientId +
                "&code_challenge=" + mVerifier + "&state=RequestID4");
        Intent intent = new Intent(Intent.ACTION_VIEW, authUri);
        startActivity(intent);
    }

    private void generateCodeVerifier(){
        SecureRandom sr = new SecureRandom();
        byte[] code = new byte[64];
        sr.nextBytes(code);
        String verify = Base64.encodeToString(code, (Base64.URL_SAFE | Base64.NO_PADDING));
        mVerifier = verify.trim().replaceAll("\n", "");
    }
}