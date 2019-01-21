package com.atta.eproperty.property_datils;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.atta.eproperty.model.APIService;
import com.atta.eproperty.model.APIUrl;
import com.atta.eproperty.model.PropertyResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PropertyDetailsPresenter implements PropertyDetailsContract.Presenter{

    ProgressDialog progressDialog;

    private PropertyDetailsContract.View mView;

    private Context mContext;

    String essentialsResults, lifestyleResults;

    public PropertyDetailsPresenter(PropertyDetailsContract.View mView, Context mContext, ProgressDialog progressDialog) {
        this.mView = mView;
        this.mContext = mContext;
        this.progressDialog = progressDialog;
        essentialsResults = "";
        lifestyleResults = "";
    }

    @Override
    public void requestPlaces(String[] url, final String type, final String[] place) {

        StringRequest[] stringRequests = new StringRequest[5];
        for (int i =0; i < 5; i++){

            if (i != 4){

                stringRequests[i] =  createStringRequest(url[i], type, place[i], false);
            }else
                stringRequests[i] =  createStringRequest(url[i], type, place[i], true);
        }

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        for (int i =0; i < 5; i++){


            requestQueue.add(stringRequests[i]);
        }
    }

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

                                mView.setViews(essentialsResults, type);
                                break;
                            case "lifestyle":

                                mView.setViews(lifestyleResults, type);
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

    @Override
    public void setResult(String result, String type, String place) {

        switch (type){
            case "essentials":

                essentialsResults = new StringBuilder().append(essentialsResults).append(result).append(" ").append(place).toString();

                break;
            case "lifestyle":

                lifestyleResults = new StringBuilder().append(lifestyleResults).append(result).append(" ").append(place).toString();

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
    public void removeFromFav(int propertyId, int userId) {

        //building retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Defining retrofit api service
        APIService service = retrofit.create(APIService.class);


        //defining the call
        Call<PropertyResult> call = service.removeFromFavorite(propertyId, userId);

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
        Call<PropertyResult> call = service.checkIfFavorite(propertyId, userId);

        //calling the api
        call.enqueue(new Callback<PropertyResult>() {
            @Override
            public void onResponse(Call<PropertyResult> call, retrofit2.Response<PropertyResult> response) {
                //hiding progress dialog
                if(progressDialog != null || progressDialog.isShowing() ){
                    progressDialog.dismiss();
                }

                //displaying the message from the response as toast
                if (response.body() != null){

                    mView.showMessage(response.body().getMessage());
                    //if there is no error
                    if (!response.body().getError()) {
                        //starting Main activity
                        mView.changeFavIcon(true);
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
}
