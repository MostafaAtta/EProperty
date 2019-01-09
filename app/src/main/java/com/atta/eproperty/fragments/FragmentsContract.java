package com.atta.eproperty.fragments;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.atta.eproperty.model.Property;
import com.squareup.okhttp.ResponseBody;

import java.util.ArrayList;

public interface FragmentsContract {

    interface View{

        void showError(String error);

        void showRecyclerView(ArrayList<Property> properties);
    }

    interface Presenter{
        void getProperties(final RecyclerView recyclerView) ;

        void getRetrofitImage(final ImageView imageView, String url) ;

        Bitmap DownloadImage(ResponseBody body);

    }
}
