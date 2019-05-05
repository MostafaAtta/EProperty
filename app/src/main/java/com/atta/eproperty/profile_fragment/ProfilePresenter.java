package com.atta.eproperty.profile_fragment;

import android.content.Context;

import com.atta.eproperty.model.APIService;
import com.atta.eproperty.model.APIUrl;
import com.atta.eproperty.model.Result;
import com.atta.eproperty.model.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfilePresenter implements ProfileContract.Presenter {

    private ProfileContract.View mView;

    private Context mContext;

    public ProfilePresenter(ProfileContract.View mView, Context mContext) {
        this.mView = mView;

        this.mContext = mContext;
    }


    @Override
    public void resetPassword(int userId, String oldPassword, String password) {

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
        Call<Result> call = service.resetPassword(userId, oldPassword, password);

        //calling the api
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

                if (response.body() != null){

                    mView.showMessage(response.body().getMessage());

                }else {
                    mView.showMessage("An error");
                }

                mView.dismissPasswordProgressDialog();
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {

                mView.showMessage(t.getMessage());
                mView.dismissPasswordProgressDialog();
            }
        });
    }

    @Override
    public void updatePhone(int userId, String phone) {

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
        Call<Result> call = service.updatePhone(userId, phone);

        //calling the api
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

                if (response.body() != null){

                    mView.showMessage(response.body().getMessage());

                    SessionManager.getInstance(mContext).setPhone(response.body().getUser().getPhone());
                    mView.updatePhoneText();

                }else {
                    mView.showMessage("An error");
                }

                mView.dismissPhoneProgressDialog();
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {

                mView.showMessage(t.getMessage());
                mView.dismissPhoneProgressDialog();
            }
        });
    }
}
