package com.em2.kstefancic.nekretnineinfo.api.model.MultiChoiceModels;

/**
 * Created by user on 7.11.2017..
 */

public class CeilingMaterial {

    private int id;
    private String ceilingMaterial;

    public CeilingMaterial(int id, String ceilingMaterial) {
        this.id = id;
        this.ceilingMaterial = ceilingMaterial;
    }

    public CeilingMaterial() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCeilingMaterial() {
        return ceilingMaterial;
    }

    public void setCeilingMaterial(String ceilingMaterial) {
        this.ceilingMaterial = ceilingMaterial;
    }
}
