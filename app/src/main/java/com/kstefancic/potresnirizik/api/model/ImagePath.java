package com.kstefancic.potresnirizik.api.model;

import java.io.Serializable;

/**
 * Created by user on 9.11.2017..
 */

public class ImagePath implements Serializable {


    private long id;

    private String path;
    private String title;

    private long buildingId;

    public ImagePath() {
    }

    public ImagePath(String path, String title,  long buildingId) {
        super();
        this.path = path;
        this.title = title;
        this.buildingId = buildingId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(long buildingId) {
        this.buildingId = buildingId;
    }
}
