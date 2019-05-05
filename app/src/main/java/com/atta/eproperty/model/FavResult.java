package com.atta.eproperty.model;

import com.google.gson.annotations.SerializedName;

public class FavResult {

    @SerializedName("favorite")
    private Boolean favorite;

    @SerializedName("id")
    private int id;

    public int getId() {
        return id;
    }


    public Boolean getFavorite() {
        return favorite;
    }
}
