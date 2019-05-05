package com.atta.eproperty.favorites_fragment;

import com.atta.eproperty.model.Property;

import java.util.ArrayList;

public interface FavoritesContract {

    interface View{

        void showError(String error);

        void showRecyclerView(ArrayList<Property> properties);

        void updateText();
    }

    interface Presenter{
        void getFavProperties(int userId) ;

    }
}
