package com.kstefancic.potresnirizik.api.model;

import com.kstefancic.potresnirizik.api.model.MultiChoiceModels.CeilingMaterial;
import com.kstefancic.potresnirizik.api.model.MultiChoiceModels.Construction;
import com.kstefancic.potresnirizik.api.model.MultiChoiceModels.Position;
import com.kstefancic.potresnirizik.api.model.MultiChoiceModels.Purpose;
import com.kstefancic.potresnirizik.api.model.MultiChoiceModels.Roof;
import com.kstefancic.potresnirizik.api.model.MultiChoiceModels.Sector;
import com.kstefancic.potresnirizik.api.model.MultiChoiceModels.SupportingSystem;

import java.util.List;

/**
 * Created by user on 3.11.2017..
 */

public class MultiChoiceDataResponse {

    private List<Construction> constructionSystem;
    // private List<Material> material;
    private List<Position> position;
    private List<Purpose> purpose;
    private List<CeilingMaterial> ceilingMaterial;
    private List<Roof> roofs;
    private List<Sector> sectors;
    private List<SupportingSystem> supportingSystems;

    public MultiChoiceDataResponse() {
    }

    public MultiChoiceDataResponse(List<Construction> constructionSystem, List<SupportingSystem> supportingSystems,
                                   List<Position> position, List<Purpose> purpose, List<CeilingMaterial> ceilingMaterial, List<Roof> roofs,
                                   List<Sector> sectors) {
        super();
        this.constructionSystem = constructionSystem;
        this.supportingSystems = supportingSystems;
        this.position = position;
        this.purpose = purpose;
        this.ceilingMaterial = ceilingMaterial;
        this.roofs = roofs;
        this.sectors = sectors;
    }

    public List<SupportingSystem> getSupportingSystems() {
        return supportingSystems;
    }

    public void setSupportingSystems(List<SupportingSystem> supportingSystems) {
        this.supportingSystems = supportingSystems;
    }

    public List<Sector> getSectors() {
        return sectors;
    }

    public void setSectors(List<Sector> sectors) {
        this.sectors = sectors;
    }

    public List<Roof> getRoofs() {
        return roofs;
    }

    public void setRoofs(List<Roof> roofs) {
        this.roofs = roofs;
    }

    public List<CeilingMaterial> getCeilingMaterial() {
        return ceilingMaterial;
    }

    public void setCeilingMaterial(List<CeilingMaterial> ceilingMaterial) {
        this.ceilingMaterial = ceilingMaterial;
    }

    public List<Construction> getConstructionSystem() {
        return constructionSystem;
    }

    public void setConstructionSystem(List<Construction> constructionSystem) {
        this.constructionSystem = constructionSystem;
    }

    // public List<Material> getMaterial() {
    // return material;
    // }
    //
    // public void setMaterial(List<Material> material) {
    // this.material = material;
    // }

    public List<Position> getPosition() {
        return position;
    }

    public void setPosition(List<Position> position) {
        this.position = position;
    }

    public List<Purpose> getPurpose() {
        return purpose;
    }

    public void setPurpose(List<Purpose> purpose) {
        this.purpose = purpose;
    }

}
