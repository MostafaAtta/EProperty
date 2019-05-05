package com.atta.eproperty.model;

import com.google.gson.annotations.SerializedName;

public class PriceResult {
    @SerializedName("error")
    private Boolean error;


    @SerializedName("price")
    private int price;


    public Boolean getError() {
        return error;
    }

    public int getPrice() {
        return price;
    }
}
