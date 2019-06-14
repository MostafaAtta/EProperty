package com.atta.eproperty.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Properties {

    @SerializedName("error")
    private Boolean error;

    @SerializedName("properties")
    private ArrayList<Property> properties;

    public Properties() {
    }

    public void setProperties(ArrayList<Property> properties) {
        this.properties = properties;
    }

    public ArrayList<Property> getProperties() {

        return properties;
    }


    public Boolean getError() {
        return error;
    }

}
