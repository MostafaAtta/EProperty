package com.atta.eproperty.model;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

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


    //the signin call
    @FormUrlEncoded
    @POST("rent_avg_price")
    Call<PriceResult> getRentAvgPrice(
            @Field("location") String location,
            @Field("type") String type
    );


    //the signin call
    @FormUrlEncoded
    @POST("sale_avg_price")
    Call<PriceResult> getSaleAvgPrice(
            @Field("location") String location,
            @Field("type") String type
    );

    @FormUrlEncoded
    @POST("add_property")
    Call<PropertyResult> addProperty(
            @FieldMap Map<String, String> images ,
            @FieldMap Map<String, String> imagesNames,
            @Field("price") int price,
            @Field("rooms") int rooms,
            @Field("baths") int baths,
            @Field("levels") String levels,
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
            @Field("type") String type,
            @Field("category") String category
    );

    @FormUrlEncoded
    @POST("add_to_favorite")
    Call<PropertyResult> addToFavorite(
            @Field("property_id") int propertyId,
            @Field("user_id") int userId
    );

    @PUT("update_phone/{id}/{phone}")
    Call<Result> updatePhone(
            @Path("id") int userId,
            @Path("phone") String phone
    );

    @PUT("update_password/{id}/{old_password}/{password}")
    Call<Result> resetPassword(
            @Path("id") int userId,
            @Path("old_password") String oldPassword,
            @Path("password") String password
    );

    @DELETE("remove_from_favorite/{id}")
    Call<PropertyResult> removeFromFavorite(
            @Path("id") int id
    );


    @FormUrlEncoded
    @POST("check_if_favorite")
    Call<FavResult> checkIfFavorite(
            @Field("property_id") int propertyId,
            @Field("user_id") int userId
    );

    @FormUrlEncoded
    @POST("get_favorite")
    Call<Properties> getFavorite(
            @Field("user_id") int userId
    );

    @FormUrlEncoded
    @POST("get_my_properties")
    Call<Properties> getMyProperties(
            @Field("user_id") int userId
    );

    @POST("get_properties")
    Call<Properties> getProperties(
    );


}
