package com.atta.eproperty.property_datils;

import com.android.volley.toolbox.StringRequest;

import java.util.Date;

public interface PropertyDetailsContract {


    interface View{

        void initiateViews();

        void setViews();

        void setViews(String result, String type);

        void setUrls();

        String formatDateTime(Date dateObject);

        String getUrl(double latitude , double longitude , String nearbyPlace);

        void showMessage(String message);

        void changeFavIcon(boolean isFav);

        void setFavId(int id);

        void setDialog();
    }

    interface Presenter{

        void requestPlaces(String[] url, final String type, final String[] place);

        StringRequest createStringRequest(String url, String type, String place, boolean endText);

        void setResult(String result, String type, String place);

        void addToFav(int propertyId, int userId);

        void removeFromFav(int fId);

        void checkIfFav(int propertyId, int userId);
    }
}
