package com.kstefancic.potresnirizik.api.model.MultiChoiceModels;

import java.io.Serializable;

/**
 * Created by user on 3.11.2017..
 */

public class Position implements Serializable {

    private int id;
    private String position;

    @Override
    public String toString() {
        return "Position{" +
                "id=" + id +
                ", position='" + position + '\'' +
                '}';
    }

    public Position() {
    }

    public Position(int id, String position) {
        this.id = id;
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
