package com.kstefancic.potresnirizik.api.exceptionutils;

import com.kstefancic.potresnirizik.api.model.ExceptionResponse;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by user on 6.11.2017..
 */

public class ErrorUtils {

    public static ExceptionResponse parseError(Response<?> response, Retrofit retrofit) {
       Converter<ResponseBody, ExceptionResponse> converter =
               retrofit.responseBodyConverter(ExceptionResponse.class, new Annotation[0]);

       ExceptionResponse exceptionResponse;

        try {
            exceptionResponse = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new ExceptionResponse();
        }

        return exceptionResponse;
    }
}
