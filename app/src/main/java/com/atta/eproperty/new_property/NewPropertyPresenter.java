package com.atta.eproperty.new_property;

import android.app.ProgressDialog;
import android.content.Context;

import com.atta.eproperty.model.APIService;
import com.atta.eproperty.model.APIUrl;
import com.atta.eproperty.model.Property;
import com.atta.eproperty.model.PropertyResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewPropertyPresenter implements NewPropertyContract.Presenter {

    ProgressDialog mProgressDialog;

    private NewPropertyContract.View mView;

    private Context mContext;

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
                property.getType()
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
}
