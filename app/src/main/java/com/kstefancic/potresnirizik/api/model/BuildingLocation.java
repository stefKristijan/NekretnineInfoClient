package com.kstefancic.potresnirizik.api.model;

import java.io.Serializable;

/**
 * Created by user on 16.11.2017..
 */

public class BuildingLocation implements Serializable {

    private long id;
    private String street;
    private int streetNumber;
    private char streetChar;
    private String settlement; // naselje
    private String cityDistrict; // gradska četvrt
    private String city;
    private String state;
    private String cadastralParticle;
    private String cadastralMunicipality;
    private long buildingId;

    @Override
    public String toString() {
        return "(KČ:"+cadastralParticle+") "+street+" "+streetNumber+ streetChar +", "+city+", "+state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BuildingLocation that = (BuildingLocation) o;

        if (streetNumber != that.streetNumber) return false;
        if (streetChar != that.streetChar) return false;
        if (!street.equals(that.street)) return false;
        if (settlement != null ? !settlement.equals(that.settlement) : that.settlement != null)
            return false;
        if (cityDistrict != null ? !cityDistrict.equals(that.cityDistrict) : that.cityDistrict != null)
            return false;
        if (!city.equals(that.city)) return false;
        if (!state.equals(that.state)) return false;
        if (!cadastralParticle.equals(that.cadastralParticle)) return false;
        return cadastralMunicipality.equals(that.cadastralMunicipality);
    }

    @Override
    public int hashCode() {
        int result = street.hashCode();
        result = 31 * result + streetNumber;
        result = 31 * result + (int) streetChar;
        result = 31 * result + (settlement != null ? settlement.hashCode() : 0);
        result = 31 * result + (cityDistrict != null ? cityDistrict.hashCode() : 0);
        result = 31 * result + city.hashCode();
        result = 31 * result + state.hashCode();
        result = 31 * result + cadastralParticle.hashCode();
        result = 31 * result + cadastralMunicipality.hashCode();
        return result;
    }

    public BuildingLocation() {
        super();
    }

    public BuildingLocation(String street, int streetNumber, char streetChar, String settlement, String cityDistrict, String city, String state, String cadastralParticle, String cadastralMunicipality) {
        this.street = street;
        this.streetNumber = streetNumber;
        this.streetChar = streetChar;
        this.settlement = settlement;
        this.cityDistrict = cityDistrict;
        this.city = city;
        this.state = state;
        this.cadastralParticle = cadastralParticle;
        this.cadastralMunicipality = cadastralMunicipality;
    }

    public String getSettlement() {
        return settlement;
    }

    public void setSettlement(String settlement) {
        this.settlement = settlement;
    }

    public String getCityDistrict() {
        return cityDistrict;
    }

    public void setCityDistrict(String cityDistrict) {
        this.cityDistrict = cityDistrict;
    }

    public String getCadastralMunicipality() {
        return cadastralMunicipality;
    }

    public void setCadastralMunicipality(String cadastralMunicipality) {
        this.cadastralMunicipality = cadastralMunicipality;
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

    public char getStreetChar() {
        return streetChar;
    }

    public void setStreetChar(char streetChar) {
        this.streetChar = streetChar;
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

    public long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(long buildingId) {
        this.buildingId = buildingId;
    }
}
