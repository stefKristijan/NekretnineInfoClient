package com.kstefancic.nekretnineinfo.api.model;

import java.io.Serializable;

/**
 * Created by user on 16.11.2017..
 */

public class BuildingLocation implements Serializable {

    private long id;
    private String street;
    private int streetNumber;
    private char streetNumberChar;
    private String city;
    private String state;
    private String cadastralParticle;
    private Building building;

    @Override
    public String toString() {
        return "Address [id=" + id + ", street=" + street + ", streetNumber=" + streetNumber + ", streetNumberChar="
                + streetNumberChar + ", city=" + city + ", state=" + state + ", building=" + building + "]";
    }

    public BuildingLocation() {
        super();
    }

    public BuildingLocation(String street, int streetNumber, char streetNumberChar, String city, String state,
                            String cadastralParticle, Building building) {
        super();
        this.street = street;
        this.streetNumber = streetNumber;
        this.streetNumberChar = streetNumberChar;
        this.city = city;
        this.state = state;
        this.cadastralParticle = cadastralParticle;
        this.building = building;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getCadastralParticle() {
        return cadastralParticle;
    }

    public void setCadastralParticle(String cadastralParticle) {
        this.cadastralParticle = cadastralParticle;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

}
