package com.kstefancic.potresnirizik.api.model.MultiChoiceModels.addressMultichoiceData;

import java.io.Serializable;

/**
 * Created by user on 17.1.2018..
 */

public class CadastralMunicipality implements Serializable {

    private int id;
    private String cadastralMunicipality;
    private long mbr;
    private int cityId;


    public CadastralMunicipality(int id, String cadastralMunicipality, long mbr, int cityId) {
        this.id = id;
        this.cadastralMunicipality = cadastralMunicipality;
        this.mbr = mbr;
        this.cityId = cityId;
    }

    public CadastralMunicipality() {
    }

    @Override
    public int hashCode() {
        return (int) (mbr ^ (mbr >>> 32));
    }

    @Override
    public String toString() {
        return "CadastralMunicipality{" +
                "id=" + id +
                ", cadastralMunicipality='" + cadastralMunicipality + '\'' +
                ", mbr=" + mbr +
                ", cityId=" + cityId +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCadastralMunicipality() {
        return cadastralMunicipality;
    }

    public void setCadastralMunicipality(String cadastralMunicipality) {
        this.cadastralMunicipality = cadastralMunicipality;
    }

    public long getMbr() {
        return mbr;
    }

    public void setMbr(long mbr) {
        this.mbr = mbr;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
