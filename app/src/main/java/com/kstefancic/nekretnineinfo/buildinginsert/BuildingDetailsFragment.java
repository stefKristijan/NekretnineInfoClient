package com.kstefancic.nekretnineinfo.buildinginsert;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.kstefancic.nekretnineinfo.R;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.CeilingMaterial;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.ConstructionSystem;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.Material;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.Purpose;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.Sector;
import com.kstefancic.nekretnineinfo.helper.DBHelper;
import com.kstefancic.nekretnineinfo.helper.PurposeExpandableListAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by user on 15.11.2017..
 */

public class BuildingDetailsFragment extends Fragment {
    private static final String FUTURE_YEAR = "Ne možete unijeti godinu veću od trenutne";
    private static final String FIRST_YEAR_GREATER = "Prva godina mora biti manja od druge godine";
    private static final String FORMAT_NOT_VALID = "Godina mora biti formata ####. ili ####.-####.";
    private Button btnNext;
    private EditText etYearOfBuild;
    private Spinner spMaterial, spCeilingMaterial, spConstructionSystem;
    private ExpandableListView expandableListView;
    private TextInputLayout tilYearOfBuild;
    private List<Material> materials;
    private List<CeilingMaterial> ceilingMaterials;
    private List<ConstructionSystem> constructionSystems;
    private List<Sector> sectors;
    private List<Purpose> purposes;
    private OtherInformationInserted otherInformationInserted;
    private PurposeExpandableListAdapter purposeExpandableListAdapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_building_details,null);
        this.sectors = DBHelper.getInstance(getActivity()).getAllSectors();
        this.purposes = DBHelper.getInstance(getActivity()).getAllPurposes();
        setUI(layout);
        return layout;
    }

    private void setUI(View layout) {
        this.spMaterial = layout.findViewById(R.id.buildingDetailsFr_spMaterials);
        this.spCeilingMaterial =layout.findViewById(R.id.buildingDetailsFr_spCeilingMaterials);
        this.spConstructionSystem =layout.findViewById(R.id.buildingDetailsFr_spConstrSys);
        this.etYearOfBuild = layout.findViewById(R.id.buildingDetailsFr_etYearOfBuild);
        this.tilYearOfBuild = layout.findViewById(R.id.buildingDetailsFr_tilYear);
        this.btnNext=layout.findViewById(R.id.buildingDetailsFr_btnNext);

        this.expandableListView = layout.findViewById(R.id.buildingDetailsFr_elvPurpose);
        this.expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                Sector sector = sectors.get(groupPosition);
                List<Purpose> purposeGroup = new ArrayList<>();

                for(Purpose purpose : purposes){
                    if(sector.getSectorName().equals(purpose.getSector().getSectorName())){
                        purposeGroup.add(purpose);
                    }
                }
                Toast.makeText(getActivity(),"Odabrali ste: "+purposeGroup.get(childPosition) ,Toast.LENGTH_SHORT).show();

                return false;
            }
        });
        setUpSpinners();
        setUpPurposeELV();
        this.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("CHECKING YEAR","before if");
                if(checkYearFormat(etYearOfBuild.getText().toString())){
                    Log.d("CHECKYEAR","good");
                }
                Material material = materials.get(spMaterial.getSelectedItemPosition());
                CeilingMaterial ceilingMaterial = ceilingMaterials.get(spCeilingMaterial.getSelectedItemPosition());
                ConstructionSystem constructionSystem = constructionSystems.get(spConstructionSystem.getSelectedItemPosition());
            }
        });

    }

    private void setUpPurposeELV() {
        this.purposeExpandableListAdapter= new PurposeExpandableListAdapter(getActivity(), sectors, purposes);
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

    private boolean checkYearFormat(String yearOfBuild){
        tilYearOfBuild.setErrorEnabled(false);
        boolean isValid = true;

        if(!Pattern.matches("^[0-9]{4}\\.$|^[0-9]{4}\\.-[0-9]{4}\\.$",yearOfBuild)){
            isValid=false;
            tilYearOfBuild.setError(FORMAT_NOT_VALID);
        }

        String[] yearParts = yearOfBuild.split("-");
        if (isValid && yearParts.length > 1) {
            int firstYear = Integer.parseInt(yearParts[0].replace(".", ""));
            int secondYear = Integer.parseInt(yearParts[1].replace(".", ""));

            if (secondYear > Calendar.getInstance().get(Calendar.YEAR)) {
                isValid=false;
                tilYearOfBuild.setError(FUTURE_YEAR);
            }
            else if (firstYear > secondYear) {
                isValid = false;
                tilYearOfBuild.setError(FIRST_YEAR_GREATER);
            }
            if(firstYear==secondYear){
                yearOfBuild= String.valueOf(firstYear)+".";
            }
        } else if(isValid){
            int year = Integer.parseInt(yearOfBuild.replace(".", ""));
            if (year > Calendar.getInstance().get(Calendar.YEAR)) {
                isValid=false;
                tilYearOfBuild.setError(FUTURE_YEAR);
            }
        }

        return isValid;
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
