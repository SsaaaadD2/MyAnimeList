package com.saadm.myanimelist.model;

import com.google.gson.annotations.SerializedName;

class ListStatus {
    @SerializedName("num_watched_episodes")
    private String numWatchedEpisodes;

    public String getNumWatchedEpisodes() {
        return numWatchedEpisodes;
    }
}
