package com.em2.kstefancic.nekretnineinfo.buildinginsert;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.em2.kstefancic.nekretnineinfo.R;

/**
 * Created by user on 15.11.2017..
 */

public class DimensionsFragment extends Fragment {

    private Button btnNext;
    private EditText etLength, etWidth, etBrutoArea, etFloorArea, etFullHeight, etFloorHeight, etNumberOfFloors;
    private DimensionsInserted dimensionsInsertedListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_dimensions,null);
        setUI(layout);
        return layout;
    }

    private void setUI(View layout) {
        this.etFloorHeight = layout.findViewById(R.id.frDims_etFloorHeight);
        this.etFloorArea = layout.findViewById(R.id.frDims_etFloorArea);
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
                double floorArea = Double.parseDouble(etFloorArea.getText().toString());
                double brutoArea = Double.parseDouble(etBrutoArea.getText().toString());
                double fullHeight = Double.parseDouble(etFullHeight.getText().toString());
                double floorHeight = Double.parseDouble(etFloorHeight.getText().toString());
                int numOfFloors = Integer.parseInt(etNumberOfFloors.getText().toString());
                dimensionsInsertedListener.onDimensionsInformationInserted(length,width,brutoArea,floorArea,fullHeight,floorHeight,numOfFloors);
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
                                             double floorArea, double fullHeight,
                                             double floorHeight, int numOfFloors);
    }

}
