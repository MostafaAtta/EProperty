package com.atta.eproperty.new_property;

import com.atta.eproperty.model.Property;

public interface NewPropertyContract {


    interface View{

        void initiateViews();

        void showViewError(String view, String error);

        void AddProperty();

        void initiateArrays();

        void showMessage(String message);

        void multiChoice();

        void navigateToMain();

        void setMovies(String movies);

        boolean validate();

        void setDialog();
    }

    interface Presenter{

        void addProperty(Property property);

    }
}
