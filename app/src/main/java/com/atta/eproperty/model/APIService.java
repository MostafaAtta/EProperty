package com.atta.eproperty.model;

import com.squareup.okhttp.ResponseBody;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
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

    @FormUrlEncoded
    @POST("add_property")
    Call<PropertyResult> addProperty(
            @FieldMap Map<String, String> images ,
            @FieldMap Map<String, String> imagesNames,
            @Field("price") int price,
            @Field("rooms") int rooms,
            @Field("baths") int baths,
            @Field("area") int area,
            @Field("address") String address,
            @Field("user_id") int userId,
            @Field("district") String district,
            @Field("city") String city,
            @FieldMap Map<String, String> amenities,
            @Field("description") String description,
            @Field("ownerPhone") String ownerPhone,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude,
            @Field("creation_time") String creationTime,
            @Field("type") String type
    );

    @FormUrlEncoded
    @POST("add_to_favorite")
    Call<PropertyResult> addToFavorite(
            @Field("property_id") int propertyId,
            @Field("user_id") int userId
    );


    @FormUrlEncoded
    @POST("remove_from_favorite")
    Call<PropertyResult> removeFromFavorite(
            @Field("property_id") int propertyId,
            @Field("user_id") int userId
    );


    @FormUrlEncoded
    @POST("check_if_favorite")
    Call<PropertyResult> checkIfFavorite(
            @Field("property_id") int propertyId,
            @Field("user_id") int userId
    );

    @FormUrlEncoded
    @POST("get_favorite")
    Call<PropertyResult> getFavorite(
            @Field("property_id") int propertyId,
            @Field("user_id") int userId
    );

    @POST("get_properties")
    Call<Properties> getProperties(
    );

    //the image call
    @retrofit.http.GET("images/{imageName}")
    retrofit.Call<ResponseBody> getImageDetails(
            @retrofit.http.Path("imageName")  String imageName);


}
