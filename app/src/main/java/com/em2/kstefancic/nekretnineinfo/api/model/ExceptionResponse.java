package com.em2.kstefancic.nekretnineinfo.api.model;

/**
 * Created by user on 6.11.2017..
 */

public class ExceptionResponse {

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}