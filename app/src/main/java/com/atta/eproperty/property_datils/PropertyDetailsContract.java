package com.atta.eproperty.property_datils;

import java.util.Date;

public interface PropertyDetailsContract {


    interface View{

        void initiateViews();

        void setViews();

        void setViews(String result, String type);

        void setUrls();

        String formatDateTime(Date dateObject);

        String getUrl(String nearbyPlace);

        void showMessage(String message);

        void changeFavIcon(boolean isFav);

        void setFavId(int id);

        void setDialog();

        void setSaleAvgPrice(int price);

        void setRentAvgPrice(int price);

        void setLifestyleAvg(int length);

        void setEssentialsAvg(int length);
    }

    interface Presenter{

        void requestPlaces(String[] urlEssentials, String[] urlLifestyle, String[] essentialsPlace, String[] lifestylePlace, String location, String type, String category);

        void createStringRequest(String url, String place, String type, int index, String location, String PropertyType, String category);

        void setResult(String result, String type, String place);

        void addToFav(int propertyId, int userId);

        void removeFromFav(int fId);

        void checkIfFav(int propertyId, int userId);

        void getAvgPrice(String location, String type, String category);
    }
}
