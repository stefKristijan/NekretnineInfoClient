package com.em2.kstefancic.nekretnineinfo.api.model;

/**
 * Created by user on 3.11.2017..
 */

public class Position {

    private int id;
    private String mPosition;

    @Override
    public String toString() {
        return "Position{" +
                "id=" + id +
                ", mPosition='" + mPosition + '\'' +
                '}';
    }

    public Position() {
    }

    public Position(int id, String mPosition) {
        this.id = id;
        this.mPosition = mPosition;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getmPosition() {
        return mPosition;
    }

    public void setmPosition(String mPosition) {
        this.mPosition = mPosition;
    }
}
