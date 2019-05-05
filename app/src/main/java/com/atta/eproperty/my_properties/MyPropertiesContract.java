package com.atta.eproperty.my_properties;

import com.atta.eproperty.model.Property;

import java.util.ArrayList;

public interface MyPropertiesContract {

    interface View{

        void showError(String error);

        void showRecyclerView(ArrayList<Property> properties);

        void updateText();
    }

    interface Presenter{
        void getMyProperties(int userId) ;

    }
}
