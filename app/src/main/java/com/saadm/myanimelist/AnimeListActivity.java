package com.saadm.myanimelist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.saadm.myanimelist.model.AnimeItemCard;
import com.saadm.myanimelist.model.Data;
import com.saadm.myanimelist.model.DataItems;
import com.saadm.myanimelist.model.Genres;
import com.saadm.myanimelist.service.adapters.RecyclerAdapter;
import com.saadm.myanimelist.service.client.MALClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AnimeListActivity extends AppCompatActivity implements RecyclerAdapter.onCardClickListener {

    RecyclerView mRecyclerView;
    Data mAnimeData;
    String mAccessToken;
    String mStatusList;
    ArrayList<AnimeItemCard> mCardList;
    ArrayList<DataItems> mDataItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_list);

        mRecyclerView = findViewById(R.id.recyclePlanWatchCard);
        mCardList = new ArrayList<AnimeItemCard>();
        SharedPreferences prefs = getSharedPreferences(getString(R.string.PREFS_KEY), MODE_PRIVATE);
        String token = prefs.getString("accessToken", null);
        mAccessToken = "Bearer " + token;
        mStatusList = getIntent().getStringExtra("listStatus");
        Log.i("Status", mStatusList);
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
        Call<Data> data = client.getAnimeListData(mAccessToken, mStatusList, "list_updated_at", 100, 0);
        data.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                mAnimeData = response.body();
                mDataItems = (ArrayList<DataItems>) mAnimeData.getDataItems();
                constructAnimeCards();
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                Toast.makeText(AnimeListActivity.this, "Could not get list", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void constructAnimeCards() {
        for(DataItems dataItem : mDataItems){
            String allGenres;
            List<Genres> genres = dataItem.getNode().getGenres();
            if(genres != null) {
                StringBuffer buffer = new StringBuffer("");
                for (Genres genre : genres) {
                    buffer.append(genre.getName() + ", ");
                }


                //Remove the last comma and space
                allGenres = buffer.substring(0, buffer.length() - 2);
            } else{
                allGenres = "";
            }
            AnimeItemCard card = new AnimeItemCard(
                    dataItem.getNode().getTitle(),
                    allGenres,
                    dataItem.getNode().getMainPicture().getMedium()
            );
            mCardList.add(card);
        }
        createRecycleView();
    }

    private void createRecycleView() {
        RecyclerAdapter adapter = new RecyclerAdapter(AnimeListActivity.this, mCardList, this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onItemClick(AnimeItemCard card) {

    }
}