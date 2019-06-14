package com.atta.eproperty.search_fragment;

import com.atta.eproperty.model.Property;

import java.util.ArrayList;

public interface SearchContract {

    interface View{

        void showError(String error);

        void showRecyclerView(ArrayList<Property> properties);

        void initiateArrays();

        void dismissDialog();
    }

    interface Presenter{
        void getProperties() ;

        void filterProperties(int minPrice, int mixPrice, int minArea, int maxArea, String district,
                              String city, String type, String category);
    }
}
