package com.kstefancic.potresnirizik.api.model;

import com.kstefancic.potresnirizik.api.model.MultiChoiceModels.CeilingMaterial;
import com.kstefancic.potresnirizik.api.model.MultiChoiceModels.Construction;
import com.kstefancic.potresnirizik.api.model.MultiChoiceModels.Position;
import com.kstefancic.potresnirizik.api.model.MultiChoiceModels.Purpose;
import com.kstefancic.potresnirizik.api.model.MultiChoiceModels.Roof;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by user on 9.11.2017..
 */

public class Building implements Serializable{

    private Long id;
    private String uId;
    private Timestamp date;

    // DETAILS PROPERTIES
    private String yearOfBuild;
    private User user;
    private Purpose purpose;
    //private Material material;
    private CeilingMaterial ceilingMaterial;
    private Construction construction;
    private Roof roof;
    private List<ImagePath> imagePaths;
    private boolean synchronizedWithDatabase;

    // LOCATION PROPERTY
    private List<BuildingLocation> locations;

    private Position position;

    // DIMENSION PROPERTIES
    private double width;
    private double length;
    //private double brutoArea;
    //private double netoArea;
    private double residentialBrutoArea;
    private double businessBrutoArea;
    private double basementBrutoArea;
    private double atticBrutoArea;
    private double floorArea;
    private double floorHeight;
    private double fullHeight;
    private int numberOfFloors;
    private int numberOfFlats;
    private boolean properGroundPlan;
    //private int numberOfResidents;
    private String companyInBuilding;
    private String maintenanceGrade;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Building building = (Building) o;

        return uId.equals(building.uId);
    }

    @Override
    public int hashCode() {
        return uId.hashCode();
    }

    @Override
    public String toString() {
        return "Building{" +
                "id=" + id +
                ", uId='" + uId + '\'' +
                ", date=" + date +
                ", yearOfBuild='" + yearOfBuild + '\'' +
                ", user=" + user +
                ", purpose=" + purpose +
                ", ceilingMaterial=" + ceilingMaterial +
                ", construction=" + construction +
                ", roof=" + roof +
                ", imagePaths=" + imagePaths +
                ", synchronizedWithDatabase=" + synchronizedWithDatabase +
                ", locations=" + locations +
                ", position=" + position +
                ", width=" + width +
                ", length=" + length +
                ", residentialBrutoArea=" + residentialBrutoArea +
                ", businessBrutoArea=" + businessBrutoArea +
                ", basementBrutoArea=" + basementBrutoArea +
                ", atticBrutoArea=" + atticBrutoArea +
                ", floorArea=" + floorArea +
                ", floorHeight=" + floorHeight +
                ", fullHeight=" + fullHeight +
                ", numberOfFloors=" + numberOfFloors +
                ", numberOfFlats=" + numberOfFlats +
                ", properGroundPlan=" + properGroundPlan +
                ", companyInBuilding='" + companyInBuilding + '\'' +
                ", maintenanceGrade='" + maintenanceGrade + '\'' +
                '}';
    }

    public Building() {
    }

    public Building(String uId, Timestamp date, String yearOfBuild, boolean properGroundPlan) {
        super();
        this.uId = uId;
        this.date = date;
        this.yearOfBuild = yearOfBuild;
        this.properGroundPlan = properGroundPlan;
    }

    public double getAtticBrutoArea() {
        return atticBrutoArea;
    }

    public void setAtticBrutoArea(double atticBrutoArea) {
        this.atticBrutoArea = atticBrutoArea;
    }

    public double getFloorArea() {
        return floorArea;
    }

    public void setFloorArea(double floorArea) {
        this.floorArea = floorArea;
    }

    public String getCompanyInBuilding() {
        return companyInBuilding;
    }

    public void setCompanyInBuilding(String companyInBuilding) {
        this.companyInBuilding = companyInBuilding;
    }

    public String getMaintenanceGrade() {
        return maintenanceGrade;
    }

    public void setMaintenanceGrade(String maintenanceGrade) {
        this.maintenanceGrade = maintenanceGrade;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public double getResidentialBrutoArea() {
        return residentialBrutoArea;
    }

    public void setResidentialBrutoArea(double residentialBrutoArea) {
        this.residentialBrutoArea = residentialBrutoArea;
    }

    public double getBusinessBrutoArea() {
        return businessBrutoArea;
    }

    public void setBusinessBrutoArea(double businessBrutoArea) {
        this.businessBrutoArea = businessBrutoArea;
    }

    public double getBasementBrutoArea() {
        return basementBrutoArea;
    }

    public void setBasementBrutoArea(double basementBrutoArea) {
        this.basementBrutoArea = basementBrutoArea;
    }

    public int getNumberOfFlats() {
        return numberOfFlats;
    }

    public void setNumberOfFlats(int numberOfFlats) {
        this.numberOfFlats = numberOfFlats;
    }

    public List<BuildingLocation> getLocations() {
        return locations;
    }

    public void setLocations(List<BuildingLocation> locations) {
        this.locations = locations;
    }

    public Roof getRoof() {
        return roof;
    }

    public void setRoof(Roof roof) {
        this.roof = roof;
    }

    public void setDimensions(double width, double length, double atticBrutoArea, double floorHeight, double fullHeight,
                              int numberOfFloors, int numberOfFlats, double residentialBrutoArea, double basementBrutoArea,
                              double floorArea) {
        this.width = width;
        this.length = length;
        this.numberOfFlats = numberOfFlats;
        this.floorHeight = floorHeight;
        this.floorArea = floorArea ;
        this.atticBrutoArea = atticBrutoArea;
        this.fullHeight = fullHeight;
        this.numberOfFloors = numberOfFloors;
        this.residentialBrutoArea = residentialBrutoArea;
        this.basementBrutoArea = basementBrutoArea;
    }


    public void setLocation(List<BuildingLocation> locations, Position position) {
        this.locations = locations;
        this.position = position;
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

    // public double getFloorArea() {
    // return floorArea;
    // }
    //
    // public void setFloorArea(double floorArea) {
    // this.floorArea = floorArea;
    // }


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

    public CeilingMaterial getCeilingMaterial() {
        return ceilingMaterial;
    }

    public void setCeilingMaterial(CeilingMaterial ceilingMaterial) {
        this.ceilingMaterial = ceilingMaterial;
    }

    public Construction getConstruction() {
        return construction;
    }

    public void setConstruction(Construction construction) {
        this.construction = construction;
    }

    public boolean isSynchronizedWithDatabase() {
        return synchronizedWithDatabase;
    }

    public void setSynchronizedWithDatabase(boolean synchronizedWithDatabase) {
        this.synchronizedWithDatabase = synchronizedWithDatabase;
    }

}
