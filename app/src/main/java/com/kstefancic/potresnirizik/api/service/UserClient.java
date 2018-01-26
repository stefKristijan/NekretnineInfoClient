package com.kstefancic.potresnirizik.api.service;

import com.kstefancic.potresnirizik.api.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by user on 2.11.2017..
 */

public interface UserClient {

    @POST("/register")
    Call<User> createAccount(@Body User user);

    @FormUrlEncoded
    @POST("/login")
    Call<User> login(@Field("username") String username, @Field("password") String password);
}
