package com.saadm.myanimelist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.saadm.myanimelist.model.AnimeDetail;
import com.saadm.myanimelist.model.Genres;
import com.saadm.myanimelist.service.client.MALClient;
import com.squareup.picasso.Picasso;

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

    String mAccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime_details);

        mPicture = findViewById(R.id.imageAnimeDetail);
        mTitle = findViewById(R.id.txtAnimeDetailTitle);
        mDescription = findViewById(R.id.txtAnimeDetailDescription);
        mProgressBar =  findViewById(R.id.progressAnimeDetails);
        mFlexBox = findViewById(R.id.flexGenres);

        whileLoading();

        SharedPreferences prefs = getSharedPreferences(getString(R.string.PREFS_KEY), MODE_PRIVATE);
        mAccessToken = "Bearer " + prefs.getString("accessToken", null);

        createClient();
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

    private void createClient() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://api.myanimelist.net/v2/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        MALClient client = retrofit.create(MALClient.class);
        int animeId = getIntent().getIntExtra("Id", 0);
        String fields = "title,main_picture,synopsis,genres";
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
    }
}