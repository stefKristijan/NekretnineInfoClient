package com.em2.kstefancic.nekretnineinfo.api.model.MultiChoiceModels;

/**
 * Created by user on 3.11.2017..
 */

public class Material {

    private int id;
    private String material;

    @Override
    public String toString() {
        return "Material{" +
                "id=" + id +
                ", material='" + material + '\'' +
                '}';
    }

    public Material() {
    }

    public Material(int id, String material) {
        this.id = id;
        this.material = material;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }
}
