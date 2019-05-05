package com.atta.eproperty.main;

import android.content.Context;

import com.atta.eproperty.QueryUtils;

public class MainPresenter implements MainContract.Presenter{


    private MainContract.View mView;

    private Context mContext;

    public MainPresenter(MainContract.View mView, Context mContext) {
        this.mView = mView;
        this.mContext = mContext;
    }

    @Override
    public void login(String email, String password) {

        QueryUtils.login( email,  password, null, mView, mContext);
    }
}
