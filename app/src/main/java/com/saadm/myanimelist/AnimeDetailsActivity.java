package com.saadm.myanimelist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.saadm.myanimelist.model.AnimeDetail;
import com.saadm.myanimelist.model.Genres;
import com.saadm.myanimelist.service.client.MALClient;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AnimeDetailsActivity extends AppCompatActivity {

    ImageView mPicture;
    TextView mTitle;
    TextView mGenres;
    TextView mDescription;
    ProgressBar mProgressBar;
    FlexboxLayout mFlexBox;
    Spinner mSpinner;

    String mAccessToken;
    String mListSelected;
    String mListDefault;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime_details);

        mPicture = findViewById(R.id.imageAnimeDetail);
        mTitle = findViewById(R.id.txtAnimeDetailTitle);
        mDescription = findViewById(R.id.txtAnimeDetailDescription);
        mProgressBar =  findViewById(R.id.progressAnimeDetails);
        mFlexBox = findViewById(R.id.flexGenres);
        mSpinner = findViewById(R.id.spinnerWatchList);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.watchlist_spinner,
                R.layout.spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getSelectedItem().toString();
                mListSelected = selected.toLowerCase().replace(" ", "_");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        whileLoading();

        SharedPreferences prefs = getSharedPreferences(getString(R.string.PREFS_KEY), MODE_PRIVATE);
        mAccessToken = "Bearer " + prefs.getString("accessToken", null);

        getAnimeInfo();
    }



    private void whileLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
        mTitle.setVisibility(View.INVISIBLE);
        mDescription.setVisibility(View.INVISIBLE);
        mPicture.setVisibility(View.INVISIBLE);
    }

    private void afterLoaded(){
        mProgressBar.setVisibility(View.INVISIBLE);
        mTitle.setVisibility(View.VISIBLE);
        mDescription.setVisibility(View.VISIBLE);
        mPicture.setVisibility(View.VISIBLE);
    }

    private void getAnimeInfo() {
        MALClient client = createClient();
        int animeId = getIntent().getIntExtra("Id", 0);
        String fields = "title,main_picture,synopsis,genres,my_list_status";
        Call<AnimeDetail> animeDetails = client.getAnimeData(mAccessToken, animeId, fields);
        animeDetails.enqueue(new Callback<AnimeDetail>() {
            @Override
            public void onResponse(Call<AnimeDetail> call, Response<AnimeDetail> response) {
                AnimeDetail detail = response.body();
                renderPage(detail);
            }

            @Override
            public void onFailure(Call<AnimeDetail> call, Throwable t) {

            }
        });
    }

    private MALClient createClient() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://api.myanimelist.net/v2/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        return retrofit.create(MALClient.class);
    }

    private void renderPage(AnimeDetail detail) {
        String pictureUrl = detail.getPicture().getMedium();
        Picasso.get().load(pictureUrl).into(mPicture);
        mTitle.setText(detail.getTitle());
        mDescription.setText(detail.getSynopsis());
        for(Genres genre : detail.getGenres()){
            View textBox = getLayoutInflater().inflate(R.layout.genres_textbox_layout, null);
            TextView genreText = textBox.findViewById(R.id.txtGenre);
            genreText.setText(genre.getName());
            mFlexBox.addView(textBox);
        }
        afterLoaded();
        if(detail.getMyListStatus() != null){
            setSpinnerDefaultValue(detail.getMyListStatus().getStatus());
        } else{
            setSpinnerDefaultValue(null);
        }

    }

    private void setSpinnerDefaultValue(String status) {
        String spinnerValue;
        String[] listArray = getResources().getStringArray(R.array.watchlist_spinner);
        if(status == null){
            spinnerValue = listArray[0];
        } else{
            switch(status){
                default:
                    spinnerValue= listArray[0];
                    break;
                case "plan_to_watch":
                    spinnerValue = listArray[1];
                    break;
                case"watching":
                    spinnerValue = listArray[2];
                    break;
                case "completed":
                    spinnerValue = listArray[3];
                    break;
            }
        }

        ArrayAdapter myAdapter =(ArrayAdapter) mSpinner.getAdapter();
        int position = myAdapter.getPosition(spinnerValue);
        mListDefault = spinnerValue;
        mSpinner.setSelection(position);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!mListDefault.equals(mListSelected)){
            MALClient client = createClient();
            int animeId = getIntent().getIntExtra("Id", 0);
            if(!mListSelected.equals("add_to_list")){
                Call<AnimeDetail> listUpdate = client.updateList(mAccessToken, animeId, mListSelected);
                listUpdate.enqueue(new Callback<AnimeDetail>() {
                    @Override
                    public void onResponse(Call<AnimeDetail> call, Response<AnimeDetail> response) {

                    }

                    @Override
                    public void onFailure(Call<AnimeDetail> call, Throwable t) {

                    }
                });
            } else{
                Call<Void> listDelete = client.deleteAnimeFromList(mAccessToken, animeId);
                listDelete.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
            }
        }

    }
}