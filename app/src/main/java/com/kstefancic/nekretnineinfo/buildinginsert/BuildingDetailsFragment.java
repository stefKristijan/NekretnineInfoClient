package com.kstefancic.nekretnineinfo.buildinginsert;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.kstefancic.nekretnineinfo.R;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.CeilingMaterial;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.ConstructionSystem;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.Material;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.Purpose;
import com.kstefancic.nekretnineinfo.helper.DBHelper;
import com.kstefancic.nekretnineinfo.helper.PurposeExpandableListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 15.11.2017..
 */

public class BuildingDetailsFragment extends Fragment {
    private Button btnNext;
    private Spinner spMaterial, spCeilingMaterial, spConstructionSystem;
    private List<Material> materials;
    private List<CeilingMaterial> ceilingMaterials;
    private List<ConstructionSystem> constructionSystems;
    private OtherInformationInserted otherInformationInserted;
    private PurposeExpandableListAdapter purposeExpandableListAdapter;
    private ExpandableListView expandableListView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_building_details,null);
        setUI(layout);
        return layout;
    }

    private void setUI(View layout) {
        this.spMaterial = layout.findViewById(R.id.buildingDetailsFr_spMaterials);
        this.spCeilingMaterial =layout.findViewById(R.id.buildingDetailsFr_spCeilingMaterials);
        this.spConstructionSystem =layout.findViewById(R.id.buildingDetailsFr_spConstrSys);
        this.btnNext=layout.findViewById(R.id.buildingDetailsFr_btnNext);
        this.expandableListView = layout.findViewById(R.id.buildingDetailsFr_elvPurpose);
        this.expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                Toast.makeText(getActivity(),materials.get(childPosition).getMaterial() ,Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        setUpSpinners();
        setUpPurposeELV();
        this.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Material material = materials.get(spMaterial.getSelectedItemPosition());
                CeilingMaterial ceilingMaterial = ceilingMaterials.get(spCeilingMaterial.getSelectedItemPosition());
                ConstructionSystem constructionSystem = constructionSystems.get(spConstructionSystem.getSelectedItemPosition());
            }
        });

    }

    private void setUpPurposeELV() {
        this.purposeExpandableListAdapter= new PurposeExpandableListAdapter(getActivity());
        this.expandableListView.setAdapter(purposeExpandableListAdapter);
    }

    private void setUpSpinners() {

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
