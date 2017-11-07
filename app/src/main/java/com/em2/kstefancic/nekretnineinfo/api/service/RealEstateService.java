package com.em2.kstefancic.nekretnineinfo.api.service;

import com.em2.kstefancic.nekretnineinfo.api.model.MultiChoiceDataResponse;
import com.em2.kstefancic.nekretnineinfo.api.model.RealEstate;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by user on 7.11.2017..
 */

public interface RealEstateService {

    @GET("/real-estates")
    Call<List<RealEstate>> getRealEstates();

}
