package com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.addressMultichoiceData;

/**
 * Created by user on 26.11.2017..
 */

public class City {

    int id;
    String cityName;
    State state;

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", cityName='" + cityName + '\'' +
                ", state=" + state +
                '}';
    }

    public City(int id, String cityName, State state) {

        this.id = id;
        this.cityName = cityName;
        this.state = state;
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

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
