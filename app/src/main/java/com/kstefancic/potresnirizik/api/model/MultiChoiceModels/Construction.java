package com.kstefancic.potresnirizik.api.model.MultiChoiceModels;

import java.io.Serializable;

/**
 * Created by user on 3.11.2017..
 */

public class Construction implements Serializable {

    private int id;
    private String construction;
    private SupportingSystem supportingSystem;

    public Construction() {
    }

    public Construction(int id, String construction) {
        this.id = id;
        this.construction = construction;
    }

    @Override
    public String toString() {
        return "Construction{" +
                "id=" + id +
                ", construction='" + construction + '\'' +
                ", supportingSystem=" + supportingSystem +
                '}';
    }

    public SupportingSystem getSupportingSystem() {
        return supportingSystem;
    }

    public void setSupportingSystem(SupportingSystem supportingSystem) {
        this.supportingSystem = supportingSystem;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getConstruction() {
        return construction;
    }

    public void setConstruction(String construction) {
        this.construction = construction;
    }
}
