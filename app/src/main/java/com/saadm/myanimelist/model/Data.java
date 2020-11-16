package com.saadm.myanimelist.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {
    private List<DataItems> data;

    public List<DataItems> getDataItems() {
        return data;
    }
}
