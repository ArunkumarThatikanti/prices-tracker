package com.example.real_timepricestracker.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.FormUrlEncoded;

import com.example.real_timepricestracker.model.LoginRequest;
import com.example.real_timepricestracker.model.LoginResponse;
import com.example.real_timepricestracker.model.Price;

import java.util.List;


public interface ApiService {
    @POST("login")
    @FormUrlEncoded
    Call<LoginResponse> login(
            @Field("email") String email,
            @Field("password") String password
    );

    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @GET("prices")
    Call<List<Price>> getPrices(@Header("Authorization") String token);
}
