package com.em2.kstefancic.nekretnineinfo.api.service;

import com.em2.kstefancic.nekretnineinfo.api.model.Building;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by user on 7.11.2017..
 */

public interface BuildingService {

    @GET("/api/users/{username}/buildings")
    Call<List<Building>> getBuildings(@Header("Authorization") String authorization, @Path("username") String username);

    @Multipart
    @POST("/api/users/{username}/buildings")
    Call<ResponseBody> uploadBuilding(@Header("Authorization") String authorization,
                                      @Path("username") String username,
                                      @Part List<MultipartBody.Part> files,
                                      @Part("building") Building building);

}
