package com.kstefancic.nekretnineinfo.helper;

import com.kstefancic.nekretnineinfo.api.model.MultichoiceLocationDataResponse;
import com.kstefancic.nekretnineinfo.api.model.User;
import com.kstefancic.nekretnineinfo.api.service.BuildingService;
import com.kstefancic.nekretnineinfo.api.service.MultiChoiceDataService;
import com.kstefancic.nekretnineinfo.api.service.UserClient;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by user on 27.11.2017..
 */

public class RetrofitSingleton {

    public static final String BASE_URL = "http://10.0.2.2:8080";
    private static Retrofit retrofit;
    private static BuildingService buildingService;
    private static UserClient userClient;
    private static MultiChoiceDataService multiChoiceDataService;

    public static Retrofit getRetrofitInstance(){
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(new OkHttpClient())
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();
        }
            return retrofit;
    }

    public static BuildingService getBuildingService() {
        if (buildingService == null) {
            buildingService = getRetrofitInstance().create(BuildingService.class);
        }
        return buildingService;
    }

    public static UserClient getUserClient() {
        if (userClient == null) {
            userClient= getRetrofitInstance().create(UserClient.class);
        }
        return userClient;
    }
    public static MultiChoiceDataService getMultiChoiceDataService(){
        if(multiChoiceDataService==null){
            multiChoiceDataService=getRetrofitInstance().create(MultiChoiceDataService.class);
        }
        return multiChoiceDataService;
    }
}
