package com.kstefancic.potresnirizik.api.model.MultiChoiceModels;

import java.io.Serializable;

/**
 * Created by user on 17.1.2018..
 */

public class SupportingSystem implements Serializable {

    private int id;
    private String supportingSystem;

    public SupportingSystem(int id, String supportingSystem) {
        this.id = id;
        this.supportingSystem = supportingSystem;
    }

    public SupportingSystem() {
    }



    @Override
    public String toString() {
        return "SupportingSystem{" +
                "id=" + id +
                ", supportingSystem='" + supportingSystem + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSupportingSystem() {
        return supportingSystem;
    }

    public void setSupportingSystem(String supportingSystem) {
        this.supportingSystem = supportingSystem;
    }
}
