package com.atta.eproperty.model;

import com.squareup.okhttp.ResponseBody;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIService {


    //The register call
    @FormUrlEncoded
    @POST("register")
    Call<Result> createUser(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("phone") String phone
    );


    //the signin call
    @FormUrlEncoded
    @POST("login")
    Call<Result> userLogin(
            @Field("email") String email,
            @Field("password") String password
    );

    @POST("get_properties")
    Call<Properties> getProperties(
    );

    //the image call
    @retrofit.http.GET("images/{imageName}")
    retrofit.Call<ResponseBody> getImageDetails(
            @retrofit.http.Path("imageName")  String imageName);


}
