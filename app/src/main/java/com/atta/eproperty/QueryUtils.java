package com.atta.eproperty;

import android.content.Context;
import android.content.Intent;

import com.atta.eproperty.login.LoginContract;
import com.atta.eproperty.main.MainContract;
import com.atta.eproperty.model.APIService;
import com.atta.eproperty.model.APIUrl;
import com.atta.eproperty.model.Result;
import com.atta.eproperty.model.SessionManager;
import com.atta.eproperty.new_property.NewPropertyActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QueryUtils{

    public static void login(String email, String password, final LoginContract.View loginView, final MainContract.View mainView
            , final Context mContext) {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService service = retrofit.create(APIService.class);


        Call<Result> call = service.userLogin(email, password);

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (loginView != null && mainView == null){

                    loginView.dismissProgressDialog();
                    if (!response.body().getError()) {
                        loginView.showMessage();
                        SessionManager.getInstance(mContext).createLoginSession(response.body().getUser());
                        loginView.navigateToMain();
                    } else {

                        loginView.showError("Invalid email or password");
                    }
                }else if (loginView == null && mainView != null){

                    mainView.dismissProgressDialog();
                    if (!response.body().getError()) {
                        mainView.showMessage("Login successfully");
                        SessionManager.getInstance(mContext).createLoginSession(response.body().getUser());
                        Intent intent = new Intent(mContext, NewPropertyActivity.class);
                        mContext.startActivity(intent);
                    } else {

                        mainView.showMessage("Invalid email or password");
                    }
                }

            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                if (loginView != null && mainView == null){

                    loginView.dismissProgressDialog();

                    loginView.showError(t.getMessage());
                }else if (loginView == null && mainView != null){

                    mainView.dismissProgressDialog();

                    mainView.showMessage(t.getMessage());
                }


            }
        });

    }

    public static boolean validate(String email, String password, final LoginContract.View loginView, final MainContract.View mainView,
                                   final Context mContex) {

        boolean valid = true;
        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


        if (loginView != null && mainView == null){

            if (!email.matches(emailPattern) || email.isEmpty())
            {
                loginView.showError("Invalid email address");
                loginView.showViewError("email","Invalid email address");
                valid = false;

            }else {

                loginView.showViewError("email",null);
            }

            if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
                loginView.showError("password must be between 4 and 10 alphanumeric characters");
                loginView.showViewError("password","password must be between 4 and 10 alphanumeric characters");
                valid = false;
            } else {
                loginView.showViewError("password",null);
            }
        }else if (loginView == null && mainView != null){

            if (!email.matches(emailPattern) || email.isEmpty())
            {
                mainView.showMessage("Invalid email address");
                valid = false;

            }

            if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
                mainView.showMessage("password must be between 4 and 10 alphanumeric characters");
                valid = false;
            }
        }


        return valid;
    }
}
