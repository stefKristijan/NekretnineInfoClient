package com.kstefancic.potresnirizik.buildingdata;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kstefancic.potresnirizik.R;
import com.kstefancic.potresnirizik.api.model.Building;
import com.kstefancic.potresnirizik.api.model.MultiChoiceModels.CeilingMaterial;
import com.kstefancic.potresnirizik.api.model.MultiChoiceModels.Construction;
import com.kstefancic.potresnirizik.api.model.MultiChoiceModels.Material;
import com.kstefancic.potresnirizik.api.model.MultiChoiceModels.Purpose;
import com.kstefancic.potresnirizik.api.model.MultiChoiceModels.Roof;
import com.kstefancic.potresnirizik.api.model.MultiChoiceModels.Sector;
import com.kstefancic.potresnirizik.helper.DBHelper;
import com.kstefancic.potresnirizik.helper.PurposeExpandableListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

import static com.kstefancic.potresnirizik.MainActivity.BUILDING_DATA;

/**
 * Created by user on 15.11.2017..
 */

public class BuildingDetailsFragment extends Fragment {
    private static final String FUTURE_YEAR = "Ne možete unijeti godinu veću od trenutne";
    private static final String FIRST_YEAR_GREATER = "Prva godina mora biti manja od druge godine";
    private static final String FORMAT_NOT_VALID = "Godina mora biti formata ####. ili ####.-####.";
    private static final String CHOOSE_PURPOSE = "Odaberite namjenu iz padajućeg izbornika";
    private Button btnAccept;
    private EditText etYearOfBuild, etCompanyInBuilding;
    private Spinner spMaterial, spCeilingMaterial, spConstructionSystem, spRoof, spMaintenanceGrade;
    private TextView tvSelectedPurpose;
    private ExpandableListView expandableListView;
    private TextInputLayout tilYearOfBuild, tilCompanyInBuilding;
    private List<Material> materials;
    private List<CeilingMaterial> ceilingMaterials;
    private List<Construction> constructions;
    private List<Roof> roofs;
    private List<Sector> sectors;
    private List<Purpose> purposes;
    private Purpose selectedPurpose;
    private BuildingDetailsInserted buildingDetailsInserted;
    private Building mBuilding;
    private PurposeExpandableListAdapter purposeExpandableListAdapter;


    public static BuildingDetailsFragment newInstance(Building building) {
        BuildingDetailsFragment fragment = new BuildingDetailsFragment();
        Bundle bundle = new Bundle();

        bundle.putSerializable(BUILDING_DATA, building);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBuilding = (Building) getArguments().getSerializable(BUILDING_DATA);

        this.sectors = DBHelper.getInstance(getActivity()).getAllSectors();
        this.purposes = DBHelper.getInstance(getActivity()).getAllPurposes();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_building_details,null);

        setUI(layout);
        return layout;
    }

    private void setUI(View layout) {
        this.spRoof = layout.findViewById(R.id.buildingDetailsFr_spRoof);
        this.tvSelectedPurpose = layout.findViewById(R.id.buildingDetailsFr_tvSelectedPurpose);
        this.spMaterial = layout.findViewById(R.id.buildingDetailsFr_spMaterials);
        this.spCeilingMaterial =layout.findViewById(R.id.buildingDetailsFr_spCeilingMaterials);
        this.spConstructionSystem =layout.findViewById(R.id.buildingDetailsFr_spConstrSys);
        this.spMaintenanceGrade = layout.findViewById(R.id.buildingDetailsFr_spMaintenanceGrade);
        this.etYearOfBuild = layout.findViewById(R.id.buildingDetailsFr_etYearOfBuild);
        this.tilYearOfBuild = layout.findViewById(R.id.buildingDetailsFr_tilYear);
        this.tilCompanyInBuilding = layout.findViewById(R.id.buildingDetailsFr_tilCompanyInBuilding);
        this.etCompanyInBuilding = layout.findViewById(R.id.buildingDetailsFr_etCompanyInBuilding);
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
                selectedPurpose = purposeGroup.get(childPosition);
                tvSelectedPurpose.setText(selectedPurpose.getPurpose());
                return false;
            }
        });

        setUpSpinners();
        setUpPurposeELV();

        if(mBuilding!=null){
            this.etYearOfBuild.setText(mBuilding.getYearOfBuild());
            this.etCompanyInBuilding.setText(mBuilding.getCompanyInBuilding());
            this.tvSelectedPurpose.setText(mBuilding.getPurpose().getPurpose());
            this.selectedPurpose=mBuilding.getPurpose();
        }

        this.btnAccept =layout.findViewById(R.id.buildingDetailsFr_btnNext);
        this.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Material material = materials.get(spMaterial.getSelectedItemPosition());
                CeilingMaterial ceilingMaterial = ceilingMaterials.get(spCeilingMaterial.getSelectedItemPosition());
                Construction construction = constructions.get(spConstructionSystem.getSelectedItemPosition());
                Roof roof = roofs.get(spRoof.getSelectedItemPosition());
                if(checkData(etYearOfBuild.getText().toString().trim(), etCompanyInBuilding.getText().toString().trim())){
                    String yearOfBuild = etYearOfBuild.getText().toString().trim();
                    String companyInBuilding = etCompanyInBuilding.getText().toString().trim();
                    String maintenanceGrade = spMaintenanceGrade.getSelectedItem().toString();
                    Log.i("BUILDING","onInsert");
                    buildingDetailsInserted.onBuildingDetailsInserted(material,ceilingMaterial, construction,roof,selectedPurpose,yearOfBuild, companyInBuilding, maintenanceGrade);
                }
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

        List<String> roofSpinnerItems = setUpRoofSpinnerItems();
        inflateSpinnerWithList(roofSpinnerItems,spRoof);

        List<String> ceilingMaterialSpinnerItems = setUpCeilingMaterialSpinnerItems();
        inflateSpinnerWithList(ceilingMaterialSpinnerItems,spCeilingMaterial);

        List<String> materialSpinnerItems = setUpMaterialSpinnerItems();
        inflateSpinnerWithList(materialSpinnerItems,spMaterial);

        inflateSpinnerWithList(Arrays.asList(getResources().getStringArray(R.array.maintenanceGrades)),spMaintenanceGrade);

        if(mBuilding!=null){
            setSpinnersToBuildingData(constSysSpinnerItems,mBuilding.getConstruction().getConstruction(),spConstructionSystem);
            setSpinnersToBuildingData(roofSpinnerItems,mBuilding.getRoof().getRoofType(),spRoof);
            setSpinnersToBuildingData(ceilingMaterialSpinnerItems,mBuilding.getCeilingMaterial().getCeilingMaterial(),spCeilingMaterial);
            setSpinnersToBuildingData(materialSpinnerItems, mBuilding.getMaterial().getMaterial(),spMaterial);
            setSpinnersToBuildingData(Arrays.asList(getResources().getStringArray(R.array.maintenanceGrades)), mBuilding.getMaintenanceGrade(), spMaintenanceGrade);
        }
    }

    private void setSpinnersToBuildingData(List<String> items, String searchTerm, Spinner spinner) {
        for(int i=0; i<items.size(); i++){
            if(items.get(i).equals(searchTerm)){
                spinner.setSelection(i);
                break;
            }
        }

    }

    private List<String> setUpRoofSpinnerItems() {
        List<String> roofsTxts = new ArrayList<>();
        roofs= DBHelper.getInstance(getActivity()).getAllRoofs();
        for(Roof roof: roofs){
            roofsTxts.add(roof.getRoofType());
        }

        return  roofsTxts;
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
            ceilingMaterialTxts.add(ceilingMaterial.getCeilingMaterial());
        }

        return  ceilingMaterialTxts;
    }

    private List<String> setUpConstrSysSpinnerItems() {
        List<String> constrSysTexts = new ArrayList<>();
        constructions = DBHelper.getInstance(getActivity()).getAllConstructionSystems();

        for(Construction construction : constructions){
            constrSysTexts.add(construction.getConstruction());
        }

        return  constrSysTexts;
    }

    private boolean checkData(String yearOfBuild, String companyInBuilding){
        tilYearOfBuild.setErrorEnabled(false);
        tilCompanyInBuilding.setErrorEnabled(false);

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

        if(selectedPurpose==null){
            isValid=false;
            Toast.makeText(getContext(), CHOOSE_PURPOSE, Toast.LENGTH_SHORT).show();
        }
        return isValid;
    }

    private void inflateSpinnerWithList(List<String> spinnerItems, Spinner spinner) {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this.getActivity(),R.layout.spinner_item,spinnerItems);

        dataAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(dataAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof BuildingDetailsInserted)
        {
            this.buildingDetailsInserted = (BuildingDetailsInserted) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.buildingDetailsInserted =null;
    }

    public interface BuildingDetailsInserted {
        void onBuildingDetailsInserted(Material wallMaterial, CeilingMaterial ceilingMaterial,
                                       Construction construction, Roof roof,
                                       Purpose purpose, String yearOfBuild, String companyInBuilding, String maintenanceGrade);
    }


}
