package com.kstefancic.potresnirizik.api.service;

import com.kstefancic.potresnirizik.api.model.Building;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by user on 7.11.2017..
 */

public interface BuildingService {

    @GET("/users/{username}/buildings")
    Call<List<Building>> getBuildings(@Header("Authorization") String authorization, @Path("username") String username);

    @Multipart
    @POST("/users/{username}/buildings")
    Call<Building> uploadBuilding(@Header("Authorization") String authorization,
                                      @Path("username") String username,
                                      @Part List<MultipartBody.Part> files,
                                      @Part("building") Building building);

}
