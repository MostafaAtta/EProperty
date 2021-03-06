package com.atta.eproperty.new_property;

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
import com.atta.eproperty.model.PriceResult;
import com.atta.eproperty.model.Property;
import com.atta.eproperty.model.PropertyResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewPropertyPresenter implements NewPropertyContract.Presenter {

    private ProgressDialog mProgressDialog;

    private NewPropertyContract.View mView;

    private Context mContext;

    private int essentialsResults, lifestyleResults;

    private RequestQueue requestQueue;

    public NewPropertyPresenter(NewPropertyContract.View view, Context context, ProgressDialog progressDialog) {
        this.mView = view;
        this.mContext = context;
        this.mProgressDialog = progressDialog;

    }

    @Override
    public void addProperty(Property property) {

        //building retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Defining retrofit api service
        APIService service = retrofit.create(APIService.class);


        //defining the call
        Call<PropertyResult> call = service.addProperty(
                property.getImages(),
                property.getImagesNames(),
                property.getPrice(),
                property.getRooms(),
                property.getBaths(),
                property.getLevels(),
                property.getArea(),
                property.getAddress(),
                property.getUserId(),
                property.getDistrict(),
                property.getCity(),
                property.getAmenities(),
                property.getDescription(),
                property.getOwnerPhone(),
                String.valueOf(property.getLatitude()),
                String.valueOf(property.getLongitude()),
                property.getCreationTime(),
                property.getType(),
                property.getCategory()
        );

        //calling the api
        call.enqueue(new Callback<PropertyResult>() {
            @Override
            public void onResponse(Call<PropertyResult> call, Response<PropertyResult> response) {
                //hiding progress dialog
                if(mProgressDialog != null || mProgressDialog.isShowing() ){
                    mProgressDialog.dismiss();
                }

                //displaying the message from the response as toast
                mView.showMessage(response.body().getMessage());
                //if there is no error
                if (!response.body().getError()) {
                    //starting Main activity
                    mView.navigateToMain();
                }
            }

            @Override
            public void onFailure(Call<PropertyResult> call, Throwable t) {

                if(mProgressDialog != null || mProgressDialog.isShowing() ){
                mProgressDialog.dismiss();
            }
                mView.showMessage(t.getMessage());
            }
        });
    }


    @Override
    public void requestPlaces(String[] urlEssentials, String[] urlLifestyle, String location, String type, String category) {



        essentialsResults = 0;

        lifestyleResults = 0;


        requestQueue = Volley.newRequestQueue(mContext);

        for (int i =0; i < 10; i++){


                if (i < 5){

                    createStringRequest(urlEssentials[i], "essentials", i, location, type, category);

                }else {

                    createStringRequest(urlLifestyle[i-5], "lifestyle", i, location, type, category);

                }
        }



    }


    public void createStringRequest(String url, final String type, final int index, final String location, final String PropertyType, final String category){

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
                            }

                            break;
                        case "lifestyle":

                            lifestyleResults += jsonArray.length();
                            if (index == 9){
                                mView.setLifestyleAvg(lifestyleResults/5);


                                if (category != null && PropertyType != null && !location.equals("")){

                                    getAvgPrice(location, PropertyType, category);
                                }
                            }

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
                public void onResponse(Call<PriceResult> call, Response<PriceResult> response) {
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

                    if(mProgressDialog != null || mProgressDialog.isShowing() ){
                        mProgressDialog.dismiss();
                    }
                    mView.showMessage(t.getMessage());
                }
            });
        }

    }


    @Override
    public void getAddress(String url) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Create an empty ArrayList that we can start adding mobiles to

                try {
                    // Create a JSONObject from the JSON response string
                    JSONObject baseJsonResponse = new JSONObject(response);
                    JSONArray baseJsonArray, jsonArray;

                    baseJsonArray = baseJsonResponse.getJSONArray("results");


                    if (!NewPropertyPresenter.this.selectAddress(baseJsonArray, "RANGE_INTERPOLATED")) {
                        if (!NewPropertyPresenter.this.selectAddress(baseJsonArray, "ROOFTOP")) {
                            if (!NewPropertyPresenter.this.selectAddress(baseJsonArray, "GEOMETRIC_CENTER")) {
                                if (!NewPropertyPresenter.this.selectAddress(baseJsonArray, "APPROXIMATE")) {
                                    mView.showMessage("no Address selected");
                                }
                            }
                        }
                    }

                } catch (JSONException e) {

                    mView.showMessage(e.getMessage());

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


                mView.showMessage(message);

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        requestQueue.add(stringRequest);
    }

    public boolean selectAddress(JSONArray baseJsonArray, String type) throws JSONException {
        JSONArray jsonArray;
        boolean selected = false;
        for (int j = 0; j < baseJsonArray.length(); j++){

            jsonArray = baseJsonArray.getJSONObject(j).getJSONArray("address_components");

            String locationType = baseJsonArray.getJSONObject(j).getJSONObject("geometry").getString("location_type");


            String formattedAddress = baseJsonArray.getJSONObject(j).getString("formatted_address");


            if (locationType.equals(type)){

                getAddress(jsonArray, formattedAddress);

                selected =  true;
                break;
            }

        }

        return selected;
    }

    public void getAddress(JSONArray jsonArray, String formattedAddress) throws JSONException {


        String city = "", area = "";
        boolean correctAddress = false;

        for (int i = 0; i < jsonArray.length(); i++){


            JSONArray typeJSONArray = jsonArray.getJSONObject(i).getJSONArray("types");

            switch (typeJSONArray.getString(0)){
                case "country":

                    correctAddress = jsonArray.getJSONObject(i).getString("long_name").equals("Egypt");

                    break;

                case "administrative_area_level_2":
                    area = jsonArray.getJSONObject(i).getString("long_name");
                    break;
                case "administrative_area_level_1":
                    city = jsonArray.getJSONObject(i).getString("long_name");
                    break;

            }



        }

        if (correctAddress){


            mView.setAddress(formattedAddress, area, city);

        }else {
            mView.showMessage("Select address in Egypt");
        }
    }
}
