package com.kstefancic.nekretnineinfo.api.model;

import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.CeilingMaterial;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.ConstructionSystem;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.Material;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.Position;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.Purpose;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by user on 9.11.2017..
 */

public class Building implements Serializable{

    private Long id;

    private Timestamp date;

    private String yearOfBuild;
    private boolean properGroundPlan;

    private User user;

    private Purpose purpose;

    private Material material;

    private CeilingMaterial ceilingMaterial;

    private ConstructionSystem constructionSystem;

    private List<ImagePath> imagePaths;
    private boolean synchronizedWithDatabase;

    // LOCATION PROPERTIES
    public enum Orientation {
        I_Z, S_J
    }

    private String cadastralParticle;

    private String street;

    private int streetNumber;

    private char streetNumberChar;
    private String city;
    private String state;
    private Orientation orientation;
    private Position position;

    // DIMENSION PROPERTIES
    private double width;
    private double length;
    private double floorArea;
    private double brutoArea;
    private double floorHeight;
    private double fullHeight;
    private int numberOfFloors;

    public Building() {
    }

    public Building(Timestamp date, String yearOfBuild, boolean properGroundPlan) {
        super();
        this.date = date;
        this.yearOfBuild = yearOfBuild;
        this.properGroundPlan = properGroundPlan;
    }

    @Override
    public String toString() {
        return "Building [id=" + id + ", date=" + date + ", yearOfBuild=" + yearOfBuild + ", properGroundPlan="
                + properGroundPlan + ", user=" + user + ", purpose=" + purpose + ", material=" + material
                + ", ceilingMaterial=" + ceilingMaterial + ", constructionSystem=" + constructionSystem
                + ", imagePaths=" + imagePaths + ", synchronizedWithDatabase=" + synchronizedWithDatabase
                + ", cadastralParticle=" + cadastralParticle + ", street=" + street + ", streetNumber=" + streetNumber
                + ", streetNumberChar=" + streetNumberChar + ", city=" + city + ", state=" + state + ", orientation="
                + orientation + ", position=" + position + ", width=" + width + ", length=" + length + ", floorArea="
                + floorArea + ", brutoArea=" + brutoArea + ", floorHeight=" + floorHeight + ", fullHeight=" + fullHeight
                + ", numberOfFloors=" + numberOfFloors + "]";
    }

    public void setDimensions(double width, double length, double floorArea, double brutoArea, double floorHeight,
                              double fullHeight, int numberOfFloors) {
        this.width = width;
        this.length = length;
        this.floorArea = floorArea;
        this.floorHeight = floorHeight;
        this.brutoArea = brutoArea;
        this.fullHeight = fullHeight;
        this.numberOfFloors = numberOfFloors;
    }

    public void setLocation(String cadastralParticle, String street, int streetNumber, char streetNumberChar,
                            String city, String state, Orientation orientation, Position position) {
        this.cadastralParticle = cadastralParticle;
        this.state = state;
        this.street = street;
        this.streetNumber = streetNumber;
        this.streetNumberChar = streetNumberChar;
        this.city = city;
        this.orientation = orientation;
        this.position = position;
    }

    public String getCadastralParticle() {
        return cadastralParticle;
    }

    public void setCadastralParticle(String cadastralParticle) {
        this.cadastralParticle = cadastralParticle;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(int streetNumber) {
        this.streetNumber = streetNumber;
    }

    public char getStreetNumberChar() {
        return streetNumberChar;
    }

    public void setStreetNumberChar(char streetNumberChar) {
        this.streetNumberChar = streetNumberChar;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
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

    public double getFullHeight() {
        return fullHeight;
    }

    public void setFullHeight(double fullHeight) {
        this.fullHeight = fullHeight;
    }

    public int getNumberOfFloors() {
        return numberOfFloors;
    }

    public void setNumberOfFloors(int numberOfFloors) {
        this.numberOfFloors = numberOfFloors;
    }

    public List<ImagePath> getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(List<ImagePath> imagePaths) {
        this.imagePaths = imagePaths;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getYearOfBuild() {
        return yearOfBuild;
    }

    public void setYearOfBuild(String yearOfBuild) {
        this.yearOfBuild = yearOfBuild;
    }

    public boolean isProperGroundPlan() {
        return properGroundPlan;
    }

    public void setProperGroundPlan(boolean properGroundPlan) {
        this.properGroundPlan = properGroundPlan;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Purpose getPurpose() {
        return purpose;
    }

    public void setPurpose(Purpose purpose) {
        this.purpose = purpose;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public CeilingMaterial getCeilingMaterial() {
        return ceilingMaterial;
    }

    public void setCeilingMaterial(CeilingMaterial ceilingMaterial) {
        this.ceilingMaterial = ceilingMaterial;
    }

    public ConstructionSystem getConstructionSystem() {
        return constructionSystem;
    }

    public void setConstructionSystem(ConstructionSystem constructionSystem) {
        this.constructionSystem = constructionSystem;
    }

    public boolean isSynchronizedWithDatabase() {
        return synchronizedWithDatabase;
    }

    public void setSynchronizedWithDatabase(boolean synchronizedWithDatabase) {
        this.synchronizedWithDatabase = synchronizedWithDatabase;
    }

}

