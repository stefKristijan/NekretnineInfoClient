package com.kstefancic.nekretnineinfo.buildingdata;

import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.kstefancic.nekretnineinfo.R;
import com.kstefancic.nekretnineinfo.api.model.Building;

import static com.kstefancic.nekretnineinfo.MainActivity.BUILDING_DATA;

/**
 * Created by user on 15.11.2017..
 */

public class DimensionsFragment extends Fragment {

    private static final String EMPTY_FIELD = "*Obavezno polje";
    private static final String VALUE_TOO_SMALL = "Vrijednost ne može biti 0 ili manja";
    private static final String BRUTO_AREA_TOO_BIG = "Bruto površina ne može biti veća od umnoška duljine i širine";
    private static final String NUM_OF_FLOORS_SMALL = "Broj katova mora biti 1 (samo prizemlje) ili veći";
    private static final String FULL_HEIGHT_SMALL = "Ukupna visina ne može biti manja od visine kata x broj katova";
    private Button btnAccept;
    private TextInputLayout tilLength, tilWidth, tilBrutoArea, tilFullHeight, tilFloorHeight, tilNumOfFloors, tilResidentArea, tilBasementArea, tilBusinessArea;
    private EditText etLength, etWidth, etBrutoArea, etFullHeight, etFloorHeight, etNumberOfFloors, etResidentArea, etBasementArea, etBusinessArea;
    private CheckBox cbProperGroundPlan;
    private DimensionsInserted dimensionsInsertedListener;

    private Building mBuilding;

    public static DimensionsFragment newInstance(Building building) {
        DimensionsFragment fragment = new DimensionsFragment();
        Bundle bundle = new Bundle();

        bundle.putSerializable(BUILDING_DATA, building);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBuilding = (Building) getArguments().getSerializable(BUILDING_DATA);
        View layout = inflater.inflate(R.layout.fragment_dimensions,null);
        setUI(layout);
        return layout;
    }

    private void setUI(View layout) {
        this.cbProperGroundPlan = layout.findViewById(R.id.frDims_cbProperGroundPlan);
        this.tilResidentArea = layout.findViewById(R.id.frDims_tilResidentArea);
        this.tilBasementArea = layout.findViewById(R.id.frDims_tilBasementArea);
        this.tilBrutoArea = layout.findViewById(R.id.frDims_tilBrutoArea);
        this.tilBusinessArea = layout.findViewById(R.id.frDims_tilBusinessArea);
        this.tilFloorHeight = layout.findViewById(R.id.frDims_tilFloorHeight);
        this.tilFullHeight = layout.findViewById(R.id.frDims_tilFullHeight);
        this.tilLength = layout.findViewById(R.id.frDims_tilLength);
        this.tilWidth = layout.findViewById(R.id.frDims_tilWidth);
        this.tilNumOfFloors = layout.findViewById(R.id.frDims_tilNumOfFloors);

        this.etBasementArea = layout.findViewById(R.id.frDims_etBasementBA);
        this.etResidentArea = layout.findViewById(R.id.frDims_etResidentialBA);
        this.etBusinessArea=layout.findViewById(R.id.frDims_etBusinessBA);
        this.etFloorHeight = layout.findViewById(R.id.frDims_etFloorHeight);
        this.etFullHeight = layout.findViewById(R.id.frDims_etFullHeight);
        this.etLength = layout.findViewById(R.id.frDims_etLength);
        this.etBrutoArea = layout.findViewById(R.id.frDims_etBrutoArea);
        this.etWidth = layout.findViewById(R.id.frDims_etWidth);
        this.etNumberOfFloors = layout.findViewById(R.id.frDims_etNumOfFloors);
        this.btnAccept =layout.findViewById(R.id.frDims_btnNext);
        this.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean properGroundPlan = cbProperGroundPlan.isChecked();
                if(checkData(properGroundPlan)){
                    double length = Double.parseDouble(etLength.getText().toString());
                    double width = Double.parseDouble(etWidth.getText().toString());
                    double brutoArea = Double.parseDouble(etBrutoArea.getText().toString());
                    double basementArea=0, residentialArea=0, businessArea=0;
                    if(!etBasementArea.getText().toString().isEmpty()){
                        basementArea = Double.parseDouble(etBasementArea.getText().toString());
                    }
                    if(!etResidentArea.getText().toString().isEmpty()){
                        residentialArea = Double.parseDouble(etResidentArea.getText().toString());
                    }
                    if(!etBusinessArea.getText().toString().isEmpty()){
                        businessArea = Double.parseDouble(etBusinessArea.getText().toString());
                    }
                    double fullHeight = Double.parseDouble(etFullHeight.getText().toString());
                    double floorHeight = Double.parseDouble(etFloorHeight.getText().toString());
                    int numOfFloors = Integer.parseInt(etNumberOfFloors.getText().toString());
                    Log.i("DIMENSIONS","onInsert");
                    dimensionsInsertedListener.onDimensionsInformationInserted(length,width,brutoArea,basementArea,residentialArea,businessArea,fullHeight,floorHeight,numOfFloors,properGroundPlan);
                }
            }
        });

        if(mBuilding!=null){
            setDataFromBuildingToViews();
        }
    }

    private void setDataFromBuildingToViews() {
        this.etLength.setText(String.valueOf(mBuilding.getLength()));
        this.etWidth.setText(String.valueOf(mBuilding.getWidth()));
        this.etFloorHeight.setText(String.valueOf(mBuilding.getFloorHeight()));
        this.etFullHeight.setText(String.valueOf(mBuilding.getFullHeight()));
        this.etNumberOfFloors.setText(String.valueOf(mBuilding.getNumberOfFloors()));
        this.etBrutoArea.setText(String.valueOf(mBuilding.getBrutoArea()));
        this.etBusinessArea.setText(String.valueOf(mBuilding.getBusinessBrutoArea()));
        this.etBasementArea.setText(String.valueOf(mBuilding.getBasementBrutoArea()));
        this.etResidentArea.setText(String.valueOf(mBuilding.getResidentialBrutoArea()));
        this.cbProperGroundPlan.setChecked(mBuilding.isProperGroundPlan());
    }

    private boolean checkData(boolean properGroundPlan) {
        boolean isValid=true;
        refreshErrors();
        String length= etLength.getText().toString();
        String width = etWidth.getText().toString();
        String brutoArea = etBrutoArea.getText().toString();
        /*String basementArea = etBasementArea.getText().toString();
        String businessArea = etBusinessArea.getText().toString();
        String residentialArea = etResidentArea.getText().toString();*/
        String fullHeight = etFullHeight.getText().toString();
        String floorHeight = etFloorHeight.getText().toString();
        String numOfFloors = etNumberOfFloors.getText().toString();

        if(length.isEmpty()){
            isValid=false;
            tilLength.setError(EMPTY_FIELD);
        }else if(Double.parseDouble(length)<=0){
            isValid = false;
            tilLength.setError(VALUE_TOO_SMALL);
        }

        if(width.isEmpty()){
            isValid=false;
            tilWidth.setError(EMPTY_FIELD);
        }else if(Double.parseDouble(width)<=0){
            isValid=false;
            tilWidth.setError(VALUE_TOO_SMALL);
        }

        if(brutoArea.isEmpty()){
            isValid=false;
            tilBrutoArea.setError(EMPTY_FIELD);
        }else if(!width.isEmpty() && !length.isEmpty() && properGroundPlan){
            if(Double.parseDouble(brutoArea)<(Double.parseDouble(width)*Double.parseDouble(length))){
                isValid=false;
                tilBrutoArea.setError(BRUTO_AREA_TOO_BIG);
            }
        }

        if(floorHeight.isEmpty()){
            isValid=false;
            tilFloorHeight.setError(EMPTY_FIELD);
        }else if(Double.parseDouble(floorHeight)<0){
            isValid=false;
            tilFloorHeight.setError(VALUE_TOO_SMALL);
        }

        if(numOfFloors.isEmpty()){
            isValid=false;
            tilNumOfFloors.setError(EMPTY_FIELD);
        }else if(Double.parseDouble(numOfFloors)<1){
            isValid=false;
            tilNumOfFloors.setError(NUM_OF_FLOORS_SMALL);
        }

        if(fullHeight.isEmpty()){
            isValid=false;
            tilFullHeight.setError(EMPTY_FIELD);
        }else if(!floorHeight.isEmpty() && !numOfFloors.isEmpty()){
            if(Double.parseDouble(fullHeight)<(Double.parseDouble(floorHeight)*Double.parseDouble(numOfFloors))){
                isValid=false;
                tilFullHeight.setError(FULL_HEIGHT_SMALL);
            }
        }


        return isValid;
    }

    private void refreshErrors() {
        tilFullHeight.setErrorEnabled(false);
        tilNumOfFloors.setErrorEnabled(false);
        tilFloorHeight.setErrorEnabled(false);
        tilBrutoArea.setErrorEnabled(false);
        tilWidth.setErrorEnabled(false);
        tilLength.setErrorEnabled(false);
        tilResidentArea.setErrorEnabled(false);
        tilBusinessArea.setErrorEnabled(false);
        tilBasementArea.setErrorEnabled(false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof DimensionsInserted)
        {
            this.dimensionsInsertedListener = (DimensionsInserted) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.dimensionsInsertedListener =null;
    }

    public interface DimensionsInserted{
        void onDimensionsInformationInserted(double length, double width, double brutoArea,
                                             double basementArea, double residentalArea, double businessArea,
                                             double fullHeight, double floorHeight, int numOfFloors, boolean properGroundPlan);
    }

}
