package com.kstefancic.nekretnineinfo.buildinginsert;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.kstefancic.nekretnineinfo.R;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.CeilingMaterial;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.ConstructionSystem;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.Material;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.Purpose;
import com.kstefancic.nekretnineinfo.helper.DBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 15.11.2017..
 */

public class OtherInformationFragment extends Fragment {
    private Button btnNext;
    private ImageButton ibCancel;
    private Spinner spMaterial, spCeilingMaterial, spConstructionSystem, spPurpose;
    private CheckBox cbProperGroundPlan;
    private List<Material> materials;
    private List<CeilingMaterial> ceilingMaterials;
    private List<ConstructionSystem> constructionSystems;
    private List<Purpose> purposes;
    private OtherInformationInserted otherInformationInserted;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_other_info,null);
        setUI(layout);
        return layout;
    }

    private void setUI(View layout) {
        this.spMaterial = layout.findViewById(R.id.frOther_spMaterials);
        this.spCeilingMaterial =layout.findViewById(R.id.frOther_spCeilingMaterials);
        this.spConstructionSystem =layout.findViewById(R.id.frOther_spConstrSys);
        this.spPurpose = layout.findViewById(R.id.frOther_spPurpose);
        this.btnNext=layout.findViewById(R.id.frOther_btnNext);
        this.cbProperGroundPlan = layout.findViewById(R.id.frOther_cbProperGroundPlan);
        setUpSpinners();
        this.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Material material = materials.get(spMaterial.getSelectedItemPosition());
                CeilingMaterial ceilingMaterial = ceilingMaterials.get(spCeilingMaterial.getSelectedItemPosition());
                Purpose purpose = purposes.get(spPurpose.getSelectedItemPosition());
                ConstructionSystem constructionSystem = constructionSystems.get(spConstructionSystem.getSelectedItemPosition());
                boolean properGroundPlan = cbProperGroundPlan.isChecked();
                otherInformationInserted.onOtherInformationInserted(material,ceilingMaterial,constructionSystem, purpose, properGroundPlan);
            }
        });
        this.ibCancel=layout.findViewById(R.id.frOther_ibCancel);
        this.ibCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                getActivity().setResult(Activity.RESULT_CANCELED, returnIntent);
                getActivity().finish();
            }
        });

    }

    private void setUpSpinners() {

        List<String> purposeSpinnerItems = setUpPurposeSpinnerItems();
        inflateSpinnerWithList(purposeSpinnerItems,spPurpose);

        List<String> constSysSpinnerItems = setUpConstrSysSpinnerItems();
        inflateSpinnerWithList(constSysSpinnerItems,spConstructionSystem);

        List<String> ceilingMaterialSpinnerItems = setUpCeilingMaterialSpinnerItems();
        inflateSpinnerWithList(ceilingMaterialSpinnerItems,spCeilingMaterial);

        List<String> materialSpinnerItems = setUpMaterialSpinnerItems();
        inflateSpinnerWithList(materialSpinnerItems,spMaterial);
    }

    private List<String> setUpMaterialSpinnerItems() {
        List<String> materialTxts = new ArrayList<>();
        materials= DBHelper.getInstance(getActivity()).getAllMaterials();

        for(Material material: materials){
            materialTxts.add(material.getMaterial());
        }

        return  materialTxts;
    }

    private List<String> setUpCeilingMaterialSpinnerItems() {
        List<String> ceilingMaterialTxts = new ArrayList<>();
        ceilingMaterials = DBHelper.getInstance(getActivity()).getAllCeilingMaterials();

        for(CeilingMaterial ceilingMaterial: ceilingMaterials){
            Log.d("SPINNER CMATERIAL",ceilingMaterial.getCeilingMaterial());
            ceilingMaterialTxts.add(ceilingMaterial.getCeilingMaterial());
        }

        return  ceilingMaterialTxts;
    }

    private List<String> setUpConstrSysSpinnerItems() {
        List<String> constrSysTexts = new ArrayList<>();
        constructionSystems = DBHelper.getInstance(getActivity()).getAllConstructionSystems();

        for(ConstructionSystem constructionSystem: constructionSystems){
            constrSysTexts.add(constructionSystem.getConstructionSystem());
        }

        return  constrSysTexts;
    }


    private List<String> setUpPurposeSpinnerItems() {
        List<String> purposeTexts = new ArrayList<>();
        purposes = DBHelper.getInstance(getActivity()).getAllPurposes();

        for(Purpose purpose: purposes){
            purposeTexts.add(purpose.getPurpose());
        }

       return  purposeTexts;
    }

    private void inflateSpinnerWithList(List<String> spinnerItems, Spinner spinner) {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this.getActivity(),R.layout.support_simple_spinner_dropdown_item,spinnerItems);

        dataAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OtherInformationInserted)
        {
            this.otherInformationInserted = (OtherInformationInserted) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.otherInformationInserted =null;
    }

    public interface OtherInformationInserted {
        void onOtherInformationInserted(Material wallMaterial, CeilingMaterial ceilingMaterial,
                                        ConstructionSystem constructionSystem, Purpose purpose,
                                        boolean properGroundPlan);
    }


}
