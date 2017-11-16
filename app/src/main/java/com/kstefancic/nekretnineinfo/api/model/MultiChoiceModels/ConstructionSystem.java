package com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels;

import java.io.Serializable;

/**
 * Created by user on 3.11.2017..
 */

public class ConstructionSystem implements Serializable {

    private int id;
    private String constructionSystem;

    public ConstructionSystem() {
    }

    public ConstructionSystem(int id, String constructionSystem) {
        this.id = id;
        this.constructionSystem = constructionSystem;
    }

    @Override
    public String toString() {
        return "ConstructionSystem{" +
                "id=" + id +
                ", constructionSystem='" + constructionSystem + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getConstructionSystem() {
        return constructionSystem;
    }

    public void setConstructionSystem(String constructionSystem) {
        this.constructionSystem = constructionSystem;
    }
}
