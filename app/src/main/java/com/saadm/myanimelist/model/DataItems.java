package com.saadm.myanimelist.model;


import com.google.gson.annotations.SerializedName;

public class DataItems {
    private DataNode node;

    @SerializedName("list_status")
    private ListStatus listStatus;

    public DataNode getNode() {
        return node;
    }
    public ListStatus getListStatus() {
        return listStatus;
    }
}
