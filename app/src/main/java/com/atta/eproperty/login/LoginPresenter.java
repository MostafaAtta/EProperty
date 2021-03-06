package com.atta.eproperty.login;

import android.app.ProgressDialog;
import android.content.Context;

import com.atta.eproperty.model.APIService;
import com.atta.eproperty.model.APIUrl;
import com.atta.eproperty.model.Result;
import com.atta.eproperty.model.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginPresenter implements LoginContract.Presenter{

    private LoginContract.View mView;

    private ProgressDialog mProgressDialog;

    private Context mContext;

    public LoginPresenter(LoginContract.View view, ProgressDialog progressDialog, Context context) {

        mView = view;

        mProgressDialog = progressDialog;

        mContext = context;
    }

    @Override
    public void login(String email, String password) {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService service = retrofit.create(APIService.class);


        Call<Result> call = service.userLogin(email, password);

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

                mView.dismissProgressDialog();
                if (response.body() != null){
                    if (!response.body().getError()) {
                        mView.showMessage();
                        SessionManager.getInstance(mContext).createLoginSession(response.body().getUser());
                        mView.navigateToMain();
                    } else {

                        mView.showError("Invalid email or password");
                    }
                }else
                    mView.showError("some thing wrong");

            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                mView.dismissProgressDialog();

                mView.showError(t.getMessage());
            }
        });

    }

    @Override
    public boolean validate(String email, String password) {

        boolean valid = true;
        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (!email.matches(emailPattern) || email.isEmpty())
        {
            mView.showError("Invalid email address");
            mView.showViewError("email","Invalid email address");
            valid = false;

        }else {

            mView.showViewError("email",null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            mView.showError("password must be between 4 and 10 alphanumeric characters");
            mView.showViewError("password","password must be between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            mView.showViewError("password",null);
        }

        return valid;
    }
}
