package com.kstefancic.nekretnineinfo.api.model;

import java.io.Serializable;

/**
 * Created by user on 9.11.2017..
 */

public class ImagePath implements Serializable {


    private int id;

    private String imagePath;

    private Building building;

    public ImagePath() {
    }

    public ImagePath(String imagePath, Building building) {
        super();
        this.imagePath = imagePath;
        this.building = building;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

}
