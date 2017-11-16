package com.kstefancic.nekretnineinfo.api.service;

import com.kstefancic.nekretnineinfo.api.model.MultiChoiceDataResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Created by user on 6.11.2017..
 */

public interface MultiChoiceDataService {

    @GET("/api/multichoice-data")
    Call<MultiChoiceDataResponse> getMultiChoiceData(@Header("Authorization") String authorization);
}
