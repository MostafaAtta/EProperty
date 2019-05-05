package com.atta.eproperty.search_fragment;

import com.atta.eproperty.model.Property;

import java.util.ArrayList;

public interface SearchContract {

    interface View{

        void showError(String error);

        void showRecyclerView(ArrayList<Property> properties);
    }

    interface Presenter{
        void getProperties() ;

    }
}
