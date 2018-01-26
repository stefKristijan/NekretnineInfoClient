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

    private List<Construction> constructions;
    // private List<Material> material;
    private List<Position> positions;
    private List<Purpose> purposes;
    private List<CeilingMaterial> ceilingMaterials;
    private List<Roof> roofs;
    private List<Sector> sectors;
    private List<SupportingSystem> supportingSystems;

    public MultiChoiceDataResponse() {
    }

    public MultiChoiceDataResponse(List<Construction> constructions, List<SupportingSystem> supportingSystems,
                                   List<Position> positions, List<Purpose> purposes, List<CeilingMaterial> ceilingMaterials, List<Roof> roofs,
                                   List<Sector> sectors) {
        super();
        this.constructions = constructions;
        this.supportingSystems = supportingSystems;
        this.positions = positions;
        this.purposes = purposes;
        this.ceilingMaterials = ceilingMaterials;
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

    public List<CeilingMaterial> getCeilingMaterials() {
        return ceilingMaterials;
    }

    public void setCeilingMaterials(List<CeilingMaterial> ceilingMaterials) {
        this.ceilingMaterials = ceilingMaterials;
    }

    public List<Construction> getConstructions() {
        return constructions;
    }

    public void setConstructions(List<Construction> constructions) {
        this.constructions = constructions;
    }

    // public List<Material> getMaterial() {
    // return material;
    // }
    //
    // public void setMaterial(List<Material> material) {
    // this.material = material;
    // }

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }

    public List<Purpose> getPurposes() {
        return purposes;
    }

    public void setPurposes(List<Purpose> purposes) {
        this.purposes = purposes;
    }

}
