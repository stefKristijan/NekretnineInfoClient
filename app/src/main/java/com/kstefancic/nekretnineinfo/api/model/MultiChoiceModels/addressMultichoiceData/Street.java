package com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.addressMultichoiceData;

/**
 * Created by user on 26.11.2017..
 */

public class Street {

    long id;
    String streetName;
    int cityId;

    public Street(long id, String streetName, int cityId) {
        this.cityId = cityId;
        this.id = id;
        this.streetName = streetName;
    }

    public Street() {
    }

    @Override
    public String toString() {
        return "Street{" + "id=" + id + ", streetName='" + streetName + '\'' + ", city=" + cityId + '}';
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }
}
