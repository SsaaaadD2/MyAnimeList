package com.saadm.myanimelist.model;

import com.google.gson.annotations.SerializedName;

public class ListStatus {
    @SerializedName("num_watched_episodes")
    private String numWatchedEpisodes;

    private String status;

    public String getStatus() {
        return status;
    }

    public String getNumWatchedEpisodes() {
        return numWatchedEpisodes;
    }
}
