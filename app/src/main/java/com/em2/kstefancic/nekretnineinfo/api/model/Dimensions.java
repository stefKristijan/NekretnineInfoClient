package com.em2.kstefancic.nekretnineinfo.api.model;

/**
 * Created by user on 7.11.2017..
 */

public class Dimensions {
    
    private Long id;
    private double width;
    private double length;
    private double floorArea;
    private double brutoArea;
    private double floorHeight;
    private double fullHeight;
    private int numberOfFloors;

    public Dimensions() {}

    public Dimensions(double width, double length, double floorArea, double brutoArea, double floorHeight,
                      double fullHeight, int numberOfFloors) {
        super();
        this.width = width;
        this.length = length;
        this.floorArea = floorArea;
        this.brutoArea = brutoArea;
        this.floorHeight = floorHeight;
        this.fullHeight = fullHeight;
        this.numberOfFloors = numberOfFloors;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public double getWidth() {
        return width;
    }
    public void setWidth(double width) {
        this.width = width;
    }
    public double getLength() {
        return length;
    }
    public void setLength(double length) {
        this.length = length;
    }
    public double getFloorArea() {
        return floorArea;
    }
    public void setFloorArea(double floorArea) {
        this.floorArea = floorArea;
    }
    public double getBrutoArea() {
        return brutoArea;
    }
    public void setBrutoArea(double brutoArea) {
        this.brutoArea = brutoArea;
    }
    public double getFloorHeight() {
        return floorHeight;
    }
    public void setFloorHeight(double floorHeight) {
        this.floorHeight = floorHeight;
    }
    public int getNumberOfFloors() {
        return numberOfFloors;
    }
    public void setNumberOfFloors(int numberOfFloors) {
        this.numberOfFloors = numberOfFloors;
    }

    public double getFullHeight() {
        return fullHeight;
    }

    public void setFullHeight(double fullHeight) {
        this.fullHeight = fullHeight;
    }
}
