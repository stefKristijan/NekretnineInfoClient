package com.em2.kstefancic.nekretnineinfo.api.model;

/**
 * Created by user on 3.11.2017..
 */

public class ConstructionSystem {

    private int id;
    private String mConstructionSystem;

    public ConstructionSystem() {
    }

    public ConstructionSystem(int id, String mConstructionSystem) {
        this.id = id;
        this.mConstructionSystem = mConstructionSystem;
    }

    @Override
    public String toString() {
        return "ConstructionSystem{" +
                "id=" + id +
                ", mConstructionSystem='" + mConstructionSystem + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getmConstructionSystem() {
        return mConstructionSystem;
    }

    public void setmConstructionSystem(String mConstructionSystem) {
        this.mConstructionSystem = mConstructionSystem;
    }
}
