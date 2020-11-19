package com.saadm.myanimelist.model;

import android.graphics.drawable.Drawable;

public class AnimeItemCard {
    private String mTitle;
//    private String mDescription;
    private String mGenres;
    private String mImageUrl;

    public AnimeItemCard(String title, /*String description,*/ String genres, String imageUrl){
        mTitle = title;
        //mDescription = description;
        mGenres = genres;
        mImageUrl = imageUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

//    public String getDescription() {
//        return mDescription;
//    }
//
//    public void setDescription(String description) {
//        mDescription = description;
//    }

    public String getGenres() {
        return mGenres;
    }

    public void setGenres(String genres) {
        mGenres = genres;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }
}
