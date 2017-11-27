package com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.addressMultichoiceData;

/**
 * Created by user on 26.11.2017..
 */

public class Street {

    int id;
    String streetName;
    City city;

    public Street(int id, String streetName, City city) {
        this.city=city;
        this.id = id;
        this.streetName = streetName;
    }

    @Override
    public String toString() {
        return "Street{" +
                "id=" + id +
                ", streetName='" + streetName + '\'' +
                ", city=" + city +
                '}';
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }
}
