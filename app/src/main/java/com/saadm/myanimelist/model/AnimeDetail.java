package com.saadm.myanimelist.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AnimeDetail {
    String title;

    @SerializedName("main_picture")
    AnimePicture mainPicture;

    @SerializedName("my_list_status")
    ListStatus myListStatus;



    String synopsis;
    List<Genres> genres;

    public String getTitle() {
        return title;
    }

    public AnimePicture getPicture() {
        return mainPicture;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public List<Genres> getGenres() {
        return genres;
    }

    public ListStatus getMyListStatus() {
        return myListStatus;
    }
}
