package com.kstefancic.potresnirizik.api.model.MultiChoiceModels;

import java.io.Serializable;

/**
 * Created by user on 16.11.2017..
 */

public class Roof implements Serializable {

    private int id;
    private String roofType;

    public Roof(int id, String roofType) {
        this.id = id;
        this.roofType = roofType;
    }

    public Roof() {
    }

    @Override
    public String toString() {
        return "Roof{" +
                "id=" + id +
                ", roofType='" + roofType + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoofType() {
        return roofType;
    }

    public void setRoofType(String roofType) {
        this.roofType = roofType;
    }
}
