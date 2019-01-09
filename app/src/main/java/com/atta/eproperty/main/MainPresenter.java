package com.atta.eproperty.main;

import android.content.Context;

public class MainPresenter implements MainContract.Presenter{


    private MainContract.View mView;

    private Context mContext;

    public MainPresenter(MainContract.View mView, Context mContext) {
        this.mView = mView;
        this.mContext = mContext;
    }

}
