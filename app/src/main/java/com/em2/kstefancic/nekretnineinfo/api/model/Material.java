package com.em2.kstefancic.nekretnineinfo.api.model;

/**
 * Created by user on 3.11.2017..
 */

public class Material {

    private int id;
    private String mMaterial;

    @Override
    public String toString() {
        return "Material{" +
                "id=" + id +
                ", mMaterial='" + mMaterial + '\'' +
                '}';
    }

    public Material() {
    }

    public Material(int id, String mMaterial) {
        this.id = id;
        this.mMaterial = mMaterial;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getmMaterial() {
        return mMaterial;
    }

    public void setmMaterial(String mMaterial) {
        this.mMaterial = mMaterial;
    }
}
