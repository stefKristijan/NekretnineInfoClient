package com.kstefancic.potresnirizik.api.model.MultiChoiceModels.addressMultichoiceData;

import java.io.Serializable;

/**
 * Created by user on 17.1.2018..
 */

public class CityDistrict implements Serializable {

    private int id;
    private String cityDistrict;
    private int cityId;

    public CityDistrict(int id, String cityDistrict, int cityId) {
        this.id = id;
        this.cityDistrict = cityDistrict;
        this.cityId = cityId;
    }

    public CityDistrict() {
    }

    @Override
    public String toString() {
        return "CityDistrict{" +
                "id=" + id +
                ", cityDistrict='" + cityDistrict + '\'' +
                ", cityId=" + cityId +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityDistrict() {
        return cityDistrict;
    }

    public void setCityDistrict(String cityDistrict) {
        this.cityDistrict = cityDistrict;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
