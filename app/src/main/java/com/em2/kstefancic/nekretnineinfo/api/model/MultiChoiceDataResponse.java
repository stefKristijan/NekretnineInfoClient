package com.em2.kstefancic.nekretnineinfo.api.model;

import com.em2.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.CeilingMaterial;
import com.em2.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.ConstructionSystem;
import com.em2.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.Material;
import com.em2.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.Position;
import com.em2.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.Purpose;

import java.util.List;

/**
 * Created by user on 3.11.2017..
 */

public class MultiChoiceDataResponse {

    private List<ConstructionSystem> constructionSystem;
    private List<Material> material;
    private List<Position> position;
    private List<Purpose> purpose;
    private List<CeilingMaterial> ceilingMaterial;

    public MultiChoiceDataResponse() {
    }

    public MultiChoiceDataResponse(List<ConstructionSystem> constructionSystem, List<Material> material, List<Position> position, List<Purpose> purpose, List<CeilingMaterial> ceilingMaterial) {
        this.constructionSystem = constructionSystem;
        this.material = material;
        this.position = position;
        this.purpose = purpose;
        this.ceilingMaterial=ceilingMaterial;
    }

    public List<CeilingMaterial> getCeilingMaterial() {
        return ceilingMaterial;
    }

    public void setCeilingMaterials(List<CeilingMaterial> ceilingMaterial) {
        this.ceilingMaterial = ceilingMaterial;
    }

    public List<ConstructionSystem> getConstructionSystem() {
        return constructionSystem;
    }

    public void setConstructionSystem(List<ConstructionSystem> constructionSystem) {
        this.constructionSystem = constructionSystem;
    }

    public List<Material> getMaterial() {
        return material;
    }

    public void setMaterial(List<Material> material) {
        this.material = material;
    }

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
