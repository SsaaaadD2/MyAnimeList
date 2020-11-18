package com.saadm.myanimelist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.saadm.myanimelist.model.Data;
import com.saadm.myanimelist.service.client.MALClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlanToWatchActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    Data mAnimeData;
    String mAccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_to_watch);

        mRecyclerView = findViewById(R.id.recyclePlanWatchCard);
        SharedPreferences prefs = getSharedPreferences(getString(R.string.PREFS_KEY), MODE_PRIVATE);
        String token = prefs.getString("accessToken", null);
        mAccessToken = "Bearer " + token;
        createMALClient();
    }

    private void createMALClient() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://api.myanimelist.net/v2/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        MALClient client = retrofit.create(MALClient.class);

        makeRequest(client);
    }

    private void makeRequest(MALClient client) {
        Call<Data> data = client.getAnimeListData(mAccessToken, "plan_to_watch", "list_updated_at", 100, 0);
        data.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                mAnimeData = response.body();
                constructAnimeCards();
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                Toast.makeText(PlanToWatchActivity.this, "Could not get list", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void constructAnimeCards() {
    }
}