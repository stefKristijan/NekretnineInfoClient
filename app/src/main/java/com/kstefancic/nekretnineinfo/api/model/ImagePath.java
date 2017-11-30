package com.kstefancic.nekretnineinfo.api.model;

import java.io.Serializable;

/**
 * Created by user on 9.11.2017..
 */

public class ImagePath implements Serializable {


    private int id;

    private String imagePath;

    private long buildingId;

    public ImagePath() {
    }

    public ImagePath(String imagePath, long buildingId) {
        super();
        this.imagePath = imagePath;
        this.buildingId = buildingId;
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

    public long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(long buildingId) {
        this.buildingId = buildingId;
    }
}
