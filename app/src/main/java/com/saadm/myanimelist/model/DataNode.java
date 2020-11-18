package com.saadm.myanimelist.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataNode {

    private List<Genres> genres;
    private String title;
    private int id;

    @SerializedName("main_picture")
    private AnimePicture mainPicture;

    public int getId() {
        return id;
    }

    public List<Genres> getGenres() {
        return genres;
    }

    public String getTitle() {
        return title;
    }

    public AnimePicture getMainPicture() {
        return mainPicture;
    }
}
