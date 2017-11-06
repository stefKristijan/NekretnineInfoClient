package com.em2.kstefancic.nekretnineinfo.api.service;

import com.em2.kstefancic.nekretnineinfo.api.model.MultiChoiceDataResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by user on 6.11.2017..
 */

public interface MultiChoiceDataService {

    @GET("/multi-choice-data")
    Call<MultiChoiceDataResponse> getMultiChoiceData();
}
