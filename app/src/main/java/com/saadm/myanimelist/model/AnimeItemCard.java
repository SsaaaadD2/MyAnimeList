package com.saadm.myanimelist.model;

import android.media.Image;

public class AnimeItemCard {
    private String mTitle;
    private String mDescription;
    private String mGenres;
    private Image mImage;

    public AnimeItemCard(String title, String description, String genres, Image image){
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

    public Image getImage() {
        return mImage;
    }

    public void setImage(Image image) {
        mImage = image;
    }
}
