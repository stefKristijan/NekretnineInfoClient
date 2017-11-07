package com.em2.kstefancic.nekretnineinfo.api.model;

import android.os.health.TimerStat;

import com.em2.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.CeilingMaterial;
import com.em2.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.ConstructionSystem;
import com.em2.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.Material;
import com.em2.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.Purpose;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;

/**
 * Created by user on 7.11.2017..
 */

public class RealEstate {

    private Long id;
    private Instant date;
    private String yearOfBuild;
    private boolean properGroundPlan;
    private User user;
    private Location location;
    private Purpose purpose;
    private Dimensions dimensions;
    private Material material;
    private CeilingMaterial ceilingMaterial;
    private ConstructionSystem constructionSystem;
    private boolean synchronizedWithDatabase;

    public RealEstate() {}

    public RealEstate(Instant date, String yearOfBuild, boolean properGroundPlan) {
        super();
        this.date = date;
        this.yearOfBuild = yearOfBuild;
        this.properGroundPlan = properGroundPlan;
    }

    @Override
    public String toString() {
        return "RealEstate{" +
                "id=" + id +
                ", date=" + date +
                ", yearOfBuild='" + yearOfBuild + '\'' +
                ", properGroundPlan=" + properGroundPlan +
                ", user=" + user +
                ", location=" + location +
                ", purpose=" + purpose +
                ", dimensions=" + dimensions +
                ", material=" + material +
                ", ceilingMaterial=" + ceilingMaterial +
                ", constructionSystem=" + constructionSystem +
                ", synchronizedWithDatabase=" + synchronizedWithDatabase +
                '}';
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Instant getDate() {
        return date;
    }
    public void setDate(Instant date) {
        this.date = date;
    }
    public String getYearOfBuild() {
        return yearOfBuild;
    }
    public void setYearOfBuild(String yearOfBuild) {
        this.yearOfBuild = yearOfBuild;
    }
    public boolean isProperGroundPlan() {
        return properGroundPlan;
    }
    public void setProperGroundPlan(boolean properGroundPlan) {
        this.properGroundPlan = properGroundPlan;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public Purpose getPurpose() {
        return purpose;
    }
    public void setPurpose(Purpose purpose) {
        this.purpose = purpose;
    }
    public Dimensions getDimensions() {
        return dimensions;
    }
    public void setDimensions(Dimensions dimensions) {
        this.dimensions = dimensions;
    }
    public Material getMaterial() {
        return material;
    }
    public void setMaterial(Material material) {
        this.material = material;
    }
    public CeilingMaterial getCeilingMaterial() {
        return ceilingMaterial;
    }
    public void setCeilingMaterial(CeilingMaterial ceilingMaterial) {
        this.ceilingMaterial = ceilingMaterial;
    }
    public ConstructionSystem getConstructionSystem() {
        return constructionSystem;
    }
    public void setConstructionSystem(ConstructionSystem constructionSystem) {
        this.constructionSystem = constructionSystem;
    }
    public boolean isSynchronizedWithDatabase() {
        return synchronizedWithDatabase;
    }
    public void setSynchronizedWithDatabase(boolean synchronizedWithDatabase) {
        this.synchronizedWithDatabase = synchronizedWithDatabase;
    }
}
