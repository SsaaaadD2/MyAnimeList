package com.saadm.myanimelist.model;

import android.graphics.drawable.Drawable;
import android.media.Image;

public class AnimeItemCard {
    private String mTitle;
    private String mDescription;
    private String mGenres;
    private Drawable mImage;

    public AnimeItemCard(String title, String description, String genres, Drawable image){
        mTitle = title;
        mDescription = description;
        mGenres = genres;
        mImage = image;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getGenres() {
        return mGenres;
    }

    public void setGenres(String genres) {
        mGenres = genres;
    }

    public Drawable getImage() {
        return mImage;
    }

    public void setImage(Drawable image) {
        mImage = image;
    }
}
