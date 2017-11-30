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
import android.widget.ImageButton;

import com.kstefancic.nekretnineinfo.R;

/**
 * Created by user on 15.11.2017..
 */

public class DimensionsFragment extends Fragment {

    private Button btnNext;
    private TextInputLayout tilLength, tilWidth, tilBrutoArea, tilFullHeight, tilFloorHeight, tilNumOfFloors, tilResidentArea, tilBasementArea, tilBusinessArea;
    private EditText etLength, etWidth, etBrutoArea, etFullHeight, etFloorHeight, etNumberOfFloors, etResidentArea, etBasementArea, etBusinessArea;
    private CheckBox cbProperGroundPlan;
    private DimensionsInserted dimensionsInsertedListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        this.btnNext=layout.findViewById(R.id.frDims_btnNext);
        this.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double length = Double.parseDouble(etLength.getText().toString());
                double width = Double.parseDouble(etWidth.getText().toString());
                double brutoArea = Double.parseDouble(etBrutoArea.getText().toString());
                double basementArea = Double.parseDouble(etBasementArea.getText().toString());
                double residentialArea = Double.parseDouble(etResidentArea.getText().toString());
                double businessArea = Double.parseDouble(etBusinessArea.getText().toString());
                double fullHeight = Double.parseDouble(etFullHeight.getText().toString());
                double floorHeight = Double.parseDouble(etFloorHeight.getText().toString());
                int numOfFloors = Integer.parseInt(etNumberOfFloors.getText().toString());
                boolean properGroundPlan = cbProperGroundPlan.isChecked();
                Log.i("DIMENSIONS","onInsert");
                dimensionsInsertedListener.onDimensionsInformationInserted(length,width,brutoArea,basementArea,residentialArea,businessArea,fullHeight,floorHeight,numOfFloors,properGroundPlan);
            }
        });

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
