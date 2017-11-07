package com.em2.kstefancic.nekretnineinfo.api.model.MultiChoiceModels;

/**
 * Created by user on 3.11.2017..
 */

public class Purpose {

    private int id;
    private String purpose;

    public Purpose() {
    }

    public Purpose(int id, String purpose) {
        this.id = id;
        this.purpose = purpose;
    }

    @Override
    public String toString() {
        return "Purpose{" +
                "id=" + id +
                ", purpose='" + purpose + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
}
