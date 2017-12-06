package com.kstefancic.nekretnineinfo.api.model.localDBdto;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by user on 16.11.2017..
 */

public class LocalImage implements Serializable {

    private int id;
    private Bitmap image;
    private String title;
    private String imagePath;
    private long buildingId;
    private int listId;

    public LocalImage(int id, String title, Bitmap image,String imagePath, long buildingId) {
        this.id = id;
        this.image = image;
        this.title = title;
        this.imagePath = imagePath;
        this.buildingId = buildingId;
    }

    public LocalImage() {
    }

    @Override
    public String toString() {
        return "LocalImage{" +
                "id=" + id +
                ", image=" + image +
                ", title=" + title+
                ", imagePath='" + imagePath + '\'' +
                ", buildingId=" + buildingId +
                ", listId=" + listId +
                '}';
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
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
