package com.saadm.myanimelist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;

import com.saadm.myanimelist.model.AnimeItemCard;
import com.saadm.myanimelist.model.Data;
import com.saadm.myanimelist.model.DataItems;
import com.saadm.myanimelist.model.Genres;
import com.saadm.myanimelist.service.adapters.RecyclerAdapter;
import com.saadm.myanimelist.service.client.MALClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AnimeListActivity extends AppCompatActivity implements RecyclerAdapter.onCardClickListener {

    //Views
    RecyclerView mRecyclerView;
    SearchView mSearchView;

    //Global variables
    Data mAnimeData;
    String mAccessToken;
    String mStatusList;
    ArrayList<AnimeItemCard> mCardList;
    ArrayList<DataItems> mDataItems;
    boolean mIsRandomAnime = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_list);

        mRecyclerView = findViewById(R.id.recyclePlanWatchCard);
        mSearchView = findViewById(R.id.searchWatchList);

        mCardList = new ArrayList<AnimeItemCard>();
        SharedPreferences prefs = getSharedPreferences(getString(R.string.PREFS_KEY), MODE_PRIVATE);
        String token = prefs.getString("accessToken", null);
        mAccessToken = "Bearer " + token;
        mStatusList = getIntent().getStringExtra("listStatus");
        createMALClient();
    }

    private void createMALClient() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://api.myanimelist.net/v2/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        MALClient client = retrofit.create(MALClient.class);

        if(mStatusList.equals("search_anime")){
            makeSearchRequest(client);
        } else{
            makeWatchListRequest(client);
        }

    }

    private void makeSearchRequest(MALClient client) {

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mCardList.clear();
                search(client, query.toLowerCase());
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mCardList.clear();
                if(newText.length() > 3) {

                    search(client, newText.toLowerCase());
                }
                return true;
            }
        });

    }

    private void search(MALClient client, String query) {
        String fields = "title,main_picture";
        Call<Data> data = client.getAnimeQueryData(mAccessToken, query, 10, 0, null);
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

    private void makeWatchListRequest(MALClient client) {
        if(mStatusList.equals("next_anime")){
            mStatusList = "plan_to_watch";
            mIsRandomAnime = true;
        }
        Call<Data> data = client.getAnimeListData(mAccessToken, mStatusList, "list_updated_at", 100, 0);
        data.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                mAnimeData = response.body();
                mDataItems = (ArrayList<DataItems>) mAnimeData.getDataItems();
                if(mIsRandomAnime){
                    getRandomAnime();
                } else{
                    constructAnimeCards();
                }

            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                Toast.makeText(AnimeListActivity.this, "Could not get list", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getRandomAnime() {
        Random rand = new Random();
        int position = rand.nextInt(mDataItems.size());

        Intent intent = new Intent(this, AnimeDetailsActivity.class);
        intent.putExtra("Id", mDataItems.get(position).getNode().getId());
        startActivity(intent);
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
            card.setId(dataItem.getNode().getId());
            mCardList.add(card);
        }
        createRecycleView();
    }

    private void createRecycleView() {
        RecyclerAdapter adapter = new RecyclerAdapter(AnimeListActivity.this, mCardList, this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        if(!mStatusList.equals("search_anime")){
            mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    if(query.length() > 3){
                        adapter.filter(query);
                    }
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    adapter.filter(newText);
                    return true;
                }
            });
        }

    }

    @Override
    public void onItemClick(AnimeItemCard card) {
        Intent intent = new Intent(this, AnimeDetailsActivity.class);
        intent.putExtra("Id", card.getId());
        startActivity(intent);
    }
}