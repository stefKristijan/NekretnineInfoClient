package com.em2.kstefancic.nekretnineinfo.api.model;

/**
 * Created by user on 3.11.2017..
 */

public class Purpose {

    private int id;
    private String mPurpose;

    public Purpose() {
    }

    public Purpose(int id, String mPurpose) {
        this.id = id;
        this.mPurpose = mPurpose;
    }

    @Override
    public String toString() {
        return "Purpose{" +
                "id=" + id +
                ", mPurpose='" + mPurpose + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getmPurpose() {
        return mPurpose;
    }

    public void setmPurpose(String mPurpose) {
        this.mPurpose = mPurpose;
    }
}
