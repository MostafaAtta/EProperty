package com.atta.eproperty.property_datils;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.atta.eproperty.model.APIService;
import com.atta.eproperty.model.APIUrl;
import com.atta.eproperty.model.FavResult;
import com.atta.eproperty.model.PriceResult;
import com.atta.eproperty.model.PropertyResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PropertyDetailsPresenter implements PropertyDetailsContract.Presenter{

    private ProgressDialog progressDialog;

    private PropertyDetailsContract.View mView;

    private Context mContext;

    private String essentialsResultsString, lifestyleResultsString;

    private int essentialsResults, lifestyleResults;

    RequestQueue requestQueue;


    public PropertyDetailsPresenter(PropertyDetailsContract.View mView, Context mContext, ProgressDialog progressDialog) {
        this.mView = mView;
        this.mContext = mContext;
        this.progressDialog = progressDialog;
    }

    @Override
    public void requestPlaces(String[] urlEssentials, String[] urlLifestyle, String[] essentialsPlace, String[] lifestylePlace, String location, String type, String category) {


        essentialsResults = 0;
        lifestyleResults = 0;

        essentialsResultsString = "";
        lifestyleResultsString = "";


        requestQueue = Volley.newRequestQueue(mContext);

        for (int i =0; i < 10; i++){


            if (i < 5){

                createStringRequest(urlEssentials[i], essentialsPlace[i],"essentials", i, location, type, category);

            }else {

                createStringRequest(urlLifestyle[i-5], lifestylePlace[i-5],"lifestyle", i, location, type, category);

            }
        }
    }
/*
    public StringRequest createStringRequest(String url, final String type, final String place, final boolean endText){
        
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Create an empty ArrayList that we can start adding mobiles to

                try {
                    // Create a JSONObject from the JSON response string
                    JSONObject baseJsonResponse = new JSONObject(response);
                    JSONArray jsonArray;

                    jsonArray = baseJsonResponse.getJSONArray("results");

                    if (endText){

                        setResult(String.valueOf(jsonArray.length()), type, place);

                        switch (type){
                            case "essentials":

                                mView.setViews(essentialsResultsString, type);


                                break;
                            case "lifestyle":

                                mView.setViews(lifestyleResultsString, type);
                                break;

                            default:
                                break;
                        }
                    }else {

                        setResult(String.valueOf(jsonArray.length()), type, place + ", ");
                    }


                    //Toast.makeText(mContext, String.valueOf(jsonArray.length()), Toast.LENGTH_SHORT).show();


                } catch (JSONException e) {

                    Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = null;
                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (error instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (error instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }


                Toast.makeText(mContext,message,Toast.LENGTH_LONG).show();

            }
        });

        return stringRequest;
    }
    */

    @Override
    public void createStringRequest(String url, final String place, final String type, final int index, final String location, final String PropertyType, final String category){

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Create an empty ArrayList that we can start adding mobiles to

                try {
                    // Create a JSONObject from the JSON response string
                    JSONObject baseJsonResponse = new JSONObject(response);
                    JSONArray jsonArray;

                    jsonArray = baseJsonResponse.getJSONArray("results");


                    switch (type){
                        case "essentials":

                            essentialsResults += jsonArray.length();
                            if (index == 4){
                                mView.setEssentialsAvg(essentialsResults/5);
                                setResult(String.valueOf(jsonArray.length()), type, place);
                                mView.setViews(essentialsResultsString, type);
                            }else
                                setResult(String.valueOf(jsonArray.length()), type, place + ", ");

                            break;
                        case "lifestyle":

                            lifestyleResults += jsonArray.length();
                            if (index == 9){
                                mView.setLifestyleAvg(lifestyleResults/5);

                                setResult(String.valueOf(jsonArray.length()), type, place);
                                mView.setViews(lifestyleResultsString, type);

                                if (category != null && PropertyType != null && !location.equals("")){

                                    getAvgPrice(location, PropertyType, category);
                                }
                            }else
                                setResult(String.valueOf(jsonArray.length()), type, place + ", ");

                            break;
                    }



//                    Toast.makeText(mContext, String.valueOf(jsonArray.length()), Toast.LENGTH_SHORT).show();


                } catch (JSONException e) {

                    Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = null;
                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (error instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (error instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }


                Toast.makeText(mContext,message,Toast.LENGTH_LONG).show();

            }
        });


        requestQueue.add(stringRequest);
    }



    @Override
    public void setResult(String result, String type, String place) {

        switch (type){
            case "essentials":

                essentialsResultsString = new StringBuilder().append(essentialsResultsString).append(result).append(" ").append(place).toString();

                break;
            case "lifestyle":

                lifestyleResultsString = new StringBuilder().append(lifestyleResultsString).append(result).append(" ").append(place).toString();

                break;

            default:
                break;
        }
    }

    @Override
    public void addToFav(int propertyId, int userId) {

        //building retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Defining retrofit api service
        APIService service = retrofit.create(APIService.class);


        //defining the call
        Call<PropertyResult> call = service.addToFavorite(propertyId, userId);

        //calling the api
        call.enqueue(new Callback<PropertyResult>() {
            @Override
            public void onResponse(Call<PropertyResult> call, retrofit2.Response<PropertyResult> response) {
                //hiding progress dialog
                if(progressDialog != null || progressDialog.isShowing() ){
                    progressDialog.dismiss();
                }

                //displaying the message from the response as toast
                if (response.body() != null) {
                    mView.showMessage(response.body().getMessage());
                    //if there is no error
                    if (!response.body().getError()) {
                        //starting Main activity
                        mView.changeFavIcon(true);
                        mView.setFavId(response.body().getId());
                    }else {
                        mView.showMessage(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<PropertyResult> call, Throwable t) {

                if(progressDialog != null || progressDialog.isShowing() ){
                    progressDialog.dismiss();
                }
                mView.showMessage(t.getMessage());
            }
        });
    }

    @Override
    public void removeFromFav(int fId) {

        //building retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Defining retrofit api service
        APIService service = retrofit.create(APIService.class);


        //defining the call
        Call<PropertyResult> call = service.removeFromFavorite(fId);

        //calling the api
        call.enqueue(new Callback<PropertyResult>() {
            @Override
            public void onResponse(Call<PropertyResult> call, retrofit2.Response<PropertyResult> response) {
                //hiding progress dialog
                if(progressDialog != null || progressDialog.isShowing() ){
                    progressDialog.dismiss();
                }

                //displaying the message from the response as toast
                if (response.body() != null) {
                    mView.showMessage(response.body().getMessage());
                    //if there is no error
                    if (!response.body().getError()) {
                        //starting Main activity
                        mView.changeFavIcon(false);
                    }else {
                        mView.showMessage(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<PropertyResult> call, Throwable t) {

                if(progressDialog != null || progressDialog.isShowing() ){
                    progressDialog.dismiss();
                }
                mView.showMessage(t.getMessage());
            }
        });
    }

    @Override
    public void checkIfFav(int propertyId, int userId) {

        //building retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Defining retrofit api service
        APIService service = retrofit.create(APIService.class);


        //defining the call
        Call<FavResult> call = service.checkIfFavorite(propertyId, userId);

        //calling the api
        call.enqueue(new Callback<FavResult>() {
            @Override
            public void onResponse(Call<FavResult> call, retrofit2.Response<FavResult> response) {
                //hiding progress dialog
                if(progressDialog != null || progressDialog.isShowing() ){
                    progressDialog.dismiss();
                }

                //displaying the message from the response as toast
                if (response.body() != null){

                    mView.changeFavIcon(response.body().getFavorite());

                    if (response.body().getFavorite()){
                        mView.setFavId(response.body().getId());
                    }

                }

            }

            @Override
            public void onFailure(Call<FavResult> call, Throwable t) {

                if(progressDialog != null || progressDialog.isShowing() ){
                    progressDialog.dismiss();
                }
                mView.showMessage(t.getMessage());
            }
        });
    }


    @Override
    public void getAvgPrice(String location, String type, final String category) {

        //building retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Defining retrofit api service
        APIService service = retrofit.create(APIService.class);

        Call<PriceResult> call = null;

        switch (category){
            case "For Sale":
                //defining the call
                call= service.getSaleAvgPrice(
                        location,
                        type
                );
                break;
            case "For Rent":
                //defining the call
                call = service.getRentAvgPrice(
                        location,
                        type
                );
                break;
        }

        if (call != null){

            //calling the api
            call.enqueue(new Callback<PriceResult>() {
                @Override
                public void onResponse(Call<PriceResult> call, retrofit2.Response<PriceResult> response) {
                    //hiding progress dialog


                    //if there is no error
                    if (!response.body().getError()) {
                        switch (category){
                            case "For Sale":
                                mView.setSaleAvgPrice(response.body().getPrice());
                                break;
                            case "For Rent":
                                mView.setRentAvgPrice(response.body().getPrice());
                                break;
                        }

                    }
                }

                @Override
                public void onFailure(Call<PriceResult> call, Throwable t) {

                    mView.showMessage(t.getMessage());
                }
            });
        }

    }
}
