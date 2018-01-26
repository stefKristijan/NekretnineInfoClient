package com.kstefancic.potresnirizik.api.model.MultiChoiceModels.addressMultichoiceData;

/**
 * Created by user on 26.11.2017..
 */

public class City {

    int id;
    String cityName;
    int stateId;

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", cityName='" + cityName + '\'' +
                ", state=" + stateId +
                '}';
    }

    public City() {
    }

    public City(int id, String cityName, int stateId) {

        this.id = id;
        this.cityName = cityName;
        this.stateId = stateId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }
}
