package com.em2.kstefancic.nekretnineinfo.api.model;

/**
 * Created by user on 9.11.2017..
 */

public class ImagePath {


    private int id;

    private String imagePath;

    private Building realEstate;

    public ImagePath() {
    }

    public ImagePath(String imagePath, Building realEstate) {
        super();
        this.imagePath = imagePath;
        this.realEstate = realEstate;
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
