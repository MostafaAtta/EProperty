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

        void setAmenities(String movies);

        boolean validate();

        void setDialog();


        void setUrls();

        String getUrl(String nearbyPlace);

        void setLifestyleAvg(int length);

        void setEssentialsAvg(int length);

        void setSaleAvgPrice(int price);

        void setRentAvgPrice(int price);

        void setAddress(String formattedAddress, String area, String city);
    }

    interface Presenter{

        void addProperty(Property property);

        void requestPlaces(String[] urlEssentials, String[] urlLifestyle, String location, String type, String category);

        void createStringRequest(String url, String type, int index, String location, String PropertyType, String category);

        void getAvgPrice(String location, String type, String category);

        void getAddress(String url);

    }
}
