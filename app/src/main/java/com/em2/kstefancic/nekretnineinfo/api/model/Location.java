package com.em2.kstefancic.nekretnineinfo.api.model;

import com.em2.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.Position;

/**
 * Created by user on 7.11.2017..
 */

public class Location {

    public enum Orientation{
        I_Z, S_J
    }

    private Long id;
    private String cadastralParticle;
    private String street;
    private int streetNumber;
    private char streetNumberChar;
    private String city;
    private String state;
    private Orientation orientation;
    private Position position;

    public Location() {}


    public Location(String cadastralParticle, String street, int streetNumber, char streetNumberChar, String city,
                    String state, Orientation orientation) {
        super();
        this.cadastralParticle = cadastralParticle;
        this.street = street;
        this.streetNumber = streetNumber;
        this.streetNumberChar = streetNumberChar;
        this.city = city;
        this.state = state;
        this.orientation = orientation;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
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
}
