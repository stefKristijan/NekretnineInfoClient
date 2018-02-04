package com.kstefancic.potresnirizik.buildingdata;

import android.app.Activity;
import android.hardware.Camera;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.kstefancic.potresnirizik.BuildingDataActivity;
import com.kstefancic.potresnirizik.R;
import com.kstefancic.potresnirizik.api.model.Building;
import com.kstefancic.potresnirizik.api.model.MultiChoiceModels.SupportingSystem;
import com.kstefancic.potresnirizik.helper.AreaCalculator;

import static com.kstefancic.potresnirizik.MainActivity.BUILDING_DATA;

/**
 * Created by user on 15.11.2017..
 */

public class DimensionsFragment extends Fragment implements DetailsUpdateListener{

    private static final String EMPTY_FIELD = "*Obavezno polje";
    private static final String VALUE_TOO_SMALL = "Vrijednost ne može biti 0 ili manja";
    private static final String BRUTO_AREA_TOO_BIG = "Bruto površina ne može biti veća od umnoška duljine i širine";
    private static final String NUM_OF_FLOORS_SMALL = "Broj katova mora biti 1 (samo prizemlje) ili veći";
    private static final String FULL_HEIGHT_SMALL = "Ukupna visina ne može biti manja od visine kata x broj katova";
    private Button btnAccept;
    private TextInputLayout tilLength, tilWidth, tilAtticArea, tilFullHeight, tilFloorHeight, tilNumOfFloors, tilResidentArea, tilBasementArea, tilBusinessArea, tilNumOfFlats, tilFloorArea;
    private EditText etLength, etWidth, etAtticArea, etFullHeight, etFloorHeight, etNumberOfFloors, etResidentArea, etBasementArea, etBusinessArea, etNumOfFlats, etFloorArea;
    private CheckBox cbProperGroundPlan;
    private DimensionsInserted dimensionsInsertedListener;
    private TextView tvFloorArea, tvBrutoArea, tvNetoArea;
    private static final String RESIDENTIAL = "Stambena zgrada";
    private static final String RES_BUSSINES = "Stambeno-poslovna zgrada";

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
        //------------------------TextInputLayouts--------------------------
        setUpTextInputLayouts(layout);
        //--------------------------EditTexts--------------------------------
        setUpEditTexts(layout);

        //-----------------------Accept Button-------------------------------
        setUpButtonAndListener(layout);

        //--------------------------Other Layouts---------------------------
        this.tvBrutoArea = layout.findViewById(R.id.frDims_tvBrutoArea);
        this.tvNetoArea = layout.findViewById(R.id.frDims_tvNetoArea);
        this.tvFloorArea = layout.findViewById(R.id.frDims_tvFloorArea);
        this.cbProperGroundPlan = layout.findViewById(R.id.frDims_cbProperGroundPlan);

        if(mBuilding!=null){
            setDataFromBuildingToViews();
        }
    }

    private void setUpButtonAndListener(View layout) {
        this.btnAccept =layout.findViewById(R.id.frDims_btnNext);
        this.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean properGroundPlan = cbProperGroundPlan.isChecked();
                if(checkData(properGroundPlan)){
                    int numofFlats = Integer.parseInt(etNumOfFlats.getText().toString().trim());
                    double length = Double.parseDouble(etLength.getText().toString().trim());
                    double width = Double.parseDouble(etWidth.getText().toString().trim());
                    double brutoArea = Double.parseDouble(etAtticArea.getText().toString().trim());
                    double basementArea=0, residentialArea=0, businessArea=0;
                    if(!etBasementArea.getText().toString().trim().isEmpty()){
                        basementArea = Double.parseDouble(etBasementArea.getText().toString().trim());
                    }
                    if(!etResidentArea.getText().toString().trim().isEmpty()){
                        residentialArea = Double.parseDouble(etResidentArea.getText().toString().trim());
                    }
                    if(!etBusinessArea.getText().toString().trim().isEmpty()){
                        businessArea = Double.parseDouble(etBusinessArea.getText().toString().trim());
                    }
                    double fullHeight = Double.parseDouble(etFullHeight.getText().toString().trim());
                    double floorHeight = Double.parseDouble(etFloorHeight.getText().toString().trim());
                    int numOfFloors = Integer.parseInt(etNumberOfFloors.getText().toString().trim());
                    Log.i("DIMENSIONS","onInsert");
                    dimensionsInsertedListener.onDimensionsInformationInserted(length,width,brutoArea,basementArea,residentialArea,businessArea,fullHeight,floorHeight,numOfFloors,numofFlats,properGroundPlan);
                }
            }
        });
    }

    private void setUpEditTexts(View layout) {
        this.etFloorArea = layout.findViewById(R.id.frDims_etFloorArea);
        this.etFloorArea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(!mBuilding.getPurpose().getPurpose().equals(RES_BUSSINES)&&
                        !mBuilding.getPurpose().getPurpose().equals(RESIDENTIAL) &&
                        etFloorArea.getText().toString().trim().equals("")){
                    String brutoArea, netoArea;
                  //  brutoArea=String.valueOf(AreaCalculator.getInstance().calculateBrutoArea())
                }

            }
        });
        this.etNumOfFlats = layout.findViewById(R.id.frDims_etNumOfFlats);
        this.etBasementArea = layout.findViewById(R.id.frDims_etBasementBA);
        this.etResidentArea = layout.findViewById(R.id.frDims_etResidentialBA);
        this.etBusinessArea=layout.findViewById(R.id.frDims_etBusinessBA);
        this.etFloorHeight = layout.findViewById(R.id.frDims_etFloorHeight);
        this.etFullHeight = layout.findViewById(R.id.frDims_etFullHeight);
        this.etLength = layout.findViewById(R.id.frDims_etLength);
        this.etAtticArea = layout.findViewById(R.id.frDims_etAtticArea);
        this.etWidth = layout.findViewById(R.id.frDims_etWidth);
        this.etNumberOfFloors = layout.findViewById(R.id.frDims_etNumOfFloors);
    }

    private void setUpTextInputLayouts(View layout) {
        this.tilFloorArea = layout.findViewById(R.id.frDims_tilFloorArea);
        this.tilResidentArea = layout.findViewById(R.id.frDims_tilResidentArea);
        this.tilBasementArea = layout.findViewById(R.id.frDims_tilBasementArea);
        this.tilAtticArea = layout.findViewById(R.id.frDims_tilAtticArea);
        this.tilBusinessArea = layout.findViewById(R.id.frDims_tilBusinessArea);
        this.tilFloorHeight = layout.findViewById(R.id.frDims_tilFloorHeight);
        this.tilFullHeight = layout.findViewById(R.id.frDims_tilFullHeight);
        this.tilLength = layout.findViewById(R.id.frDims_tilLength);
        this.tilWidth = layout.findViewById(R.id.frDims_tilWidth);
        this.tilNumOfFloors = layout.findViewById(R.id.frDims_tilNumOfFloors);
        this.tilNumOfFlats = layout.findViewById(R.id.frDims_tilNumOfFlats);
    }

    private void setDataFromBuildingToViews() {
        this.etLength.setText(String.valueOf(mBuilding.getLength()));
        this.etWidth.setText(String.valueOf(mBuilding.getWidth()));
        this.etFloorHeight.setText(String.valueOf(mBuilding.getFloorHeight()));
        this.etFullHeight.setText(String.valueOf(mBuilding.getFullHeight()));
        this.etNumberOfFloors.setText(String.valueOf(mBuilding.getNumberOfFloors()));
        this.etAtticArea.setText(String.valueOf(mBuilding.getAtticBrutoArea()));
        this.etBusinessArea.setText(String.valueOf(mBuilding.getBusinessBrutoArea()));
        this.etBasementArea.setText(String.valueOf(mBuilding.getBasementBrutoArea()));
        this.etResidentArea.setText(String.valueOf(mBuilding.getResidentialBrutoArea()));
        this.etNumOfFlats.setText(String.valueOf(mBuilding.getNumberOfFlats()));
        this.cbProperGroundPlan.setChecked(mBuilding.isProperGroundPlan());
        this.tvFloorArea.setVisibility(View.VISIBLE);
        this.tvFloorArea.setText("Katna ploština: " + AreaCalculator.getInstance().calculateFloorArea(mBuilding));
        this.tvBrutoArea.setVisibility(View.VISIBLE);
        this.tvBrutoArea.setText("Bruto ploština: " + AreaCalculator.getInstance().calculateBrutoArea(mBuilding));
        this.tvNetoArea.setVisibility(View.VISIBLE);
        this.tvNetoArea.setText("Neto ploština: "+ AreaCalculator.getInstance().calculateNetoArea(mBuilding));
    }

    private boolean checkData(boolean properGroundPlan) {
        boolean isValid=true;
        refreshErrors();
        String length= etLength.getText().toString().trim();
        String width = etWidth.getText().toString().trim();
        String brutoArea = etAtticArea.getText().toString().trim();
        /*String basementArea = etBasementArea.getText().toString();
        String businessArea = etBusinessArea.getText().toString();
        String residentialArea = etResidentArea.getText().toString();*/
        String fullHeight = etFullHeight.getText().toString().trim();
        String floorHeight = etFloorHeight.getText().toString().trim();
        String numOfFloors = etNumberOfFloors.getText().toString().trim();
        String numOfFlats = etNumOfFlats.getText().toString().trim();

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
            tilAtticArea.setError(EMPTY_FIELD);
        }else if(!width.isEmpty() && !length.isEmpty() && properGroundPlan){
            if(Double.parseDouble(brutoArea)<(Double.parseDouble(width)*Double.parseDouble(length))){
                isValid=false;
                tilAtticArea.setError(BRUTO_AREA_TOO_BIG);
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
        tilAtticArea.setErrorEnabled(false);
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
        if(context instanceof BuildingDataActivity){
            Log.d("FRAG", "updateDetailsInic");
            ((BuildingDataActivity) getActivity()).registerDataUpdateListener(this);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.dimensionsInsertedListener =null;
        ((BuildingDataActivity) getActivity()).unregisterDataUpdateListener();
    }

    @Override
    public void onDetailsUpdate(Building mBuilding) {
        switch (mBuilding.getPurpose().getPurpose()){
            case RESIDENTIAL:
                tilFloorArea.setVisibility(View.GONE);
                tilResidentArea.setVisibility(View.VISIBLE);
                tilBusinessArea.setVisibility(View.GONE);
                tvFloorArea.setVisibility(View.VISIBLE);
                tilNumOfFlats.setVisibility(View.VISIBLE);
                break;

            case RES_BUSSINES:
                tilFloorArea.setVisibility(View.GONE);
                tvFloorArea.setVisibility(View.VISIBLE);
                tilNumOfFlats.setVisibility(View.VISIBLE);
                tilResidentArea.setVisibility(View.VISIBLE);
                tilBusinessArea.setVisibility(View.VISIBLE);
                break;

            default:
                tilFloorArea.setVisibility(View.VISIBLE);
                tvFloorArea.setVisibility(View.GONE);
                tilBusinessArea.setVisibility(View.GONE);
                tilResidentArea.setVisibility(View.GONE);
                tilNumOfFlats.setVisibility(View.GONE);
                break;
        }
    }


    public interface DimensionsInserted{
        void onDimensionsInformationInserted(double length, double width, double brutoArea,
                                             double basementArea, double residentalArea, double businessArea,
                                             double fullHeight, double floorHeight, int numOfFloors, int numOfFlats, boolean properGroundPlan);
    }

}
