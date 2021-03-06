package com.atta.eproperty.my_properties;

import android.content.Context;

import com.atta.eproperty.model.APIService;
import com.atta.eproperty.model.APIUrl;
import com.atta.eproperty.model.Properties;
import com.atta.eproperty.model.Property;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MyPropertiesPresenter implements MyPropertiesContract.Presenter {

    private MyPropertiesContract.View mView;

    private Context mContext;

    public MyPropertiesPresenter(MyPropertiesContract.View mView, Context mContext) {
        this.mView = mView;
        this.mContext = mContext;
    }

    @Override
    public void getMyProperties(int userId) {

        //building retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                .build();

        //Defining retrofit api service
        APIService service = retrofit.create(APIService.class);

        //Defining the user object as we need to pass it with the call
        //User user = new User(name, email, password, phone, birthdayString, locationSting);

        //defining the call
        Call<Properties> call = service.getMyProperties(userId);

        //calling the api
        call.enqueue(new Callback<Properties>() {
            @Override
            public void onResponse(Call<Properties> call, Response<Properties> response) {

                if (response.body() != null){
                    if (response.body().getProperties() != null){

                        ArrayList<Property> properties = response.body().getProperties();

                        if (properties.size() > 0){

                            mView.showRecyclerView(properties);
                        }else {

                            mView.updateText();
                        }
                    }
                }else {
                    mView.showError("An error");
                }

            }

            @Override
            public void onFailure(Call<Properties> call, Throwable t) {

                mView.showError(t.getMessage());
            }
        });
    }

}
