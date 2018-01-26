package com.kstefancic.potresnirizik.api.model.MultiChoiceModels.addressMultichoiceData;

import java.io.Serializable;

/**
 * Created by user on 17.1.2018..
 */

public class Settlement implements Serializable {

    private int id;
    private String settlement;
    private int cityId;

    public Settlement(int id, String settlement, int cityId) {
        this.id = id;
        this.settlement = settlement;
        this.cityId = cityId;
    }

    public Settlement() {
    }

    @Override
    public String toString() {
        return "Settlement{" +
                "id=" + id +
                ", settlement='" + settlement + '\'' +
                ", cityId=" + cityId +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSettlement() {
        return settlement;
    }

    public void setSettlement(String settlement) {
        this.settlement = settlement;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
