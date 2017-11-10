package com.em2.kstefancic.nekretnineinfo.api.service;

import com.em2.kstefancic.nekretnineinfo.api.model.Building;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by user on 7.11.2017..
 */

public interface BuildingService {

    @GET("/buildings")
    Call<List<Building>> getBuildings();

    @Multipart
    @POST("/upload")
    Call<ResponseBody> uploadBuilding(@Part List<MultipartBody.Part> files, @Part("building") Building building);

}
