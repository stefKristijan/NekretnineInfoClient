package com.kstefancic.nekretnineinfo.buildinginsert;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.kstefancic.nekretnineinfo.R;
import com.kstefancic.nekretnineinfo.api.model.Building;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.Position;
import com.kstefancic.nekretnineinfo.helper.DBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 15.11.2017..
 */

public class AddressInformationFragment extends Fragment {

    private Button btnNext;
    private ImageView ibCancel;
    private EditText etStreet, etStreetNum, etStreetChar, etCity, etState, etCadastralParticle;
    private Spinner spOrientation, spPosition;
    private List<Position> positions;
    private AddressInformationInserted addressInformationInsertedListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_adress_information,null);
        setUI(layout);
        setUpSpinners();
        return layout;
    }

    private void setUI(View layout) {
        this.etCadastralParticle = layout.findViewById(R.id.frAddressInfo_etCadastralPart);
        this.etCity = layout.findViewById(R.id.frAddressInfo_etCity);
        this.etState = layout.findViewById(R.id.frAddressInfo_etState);
        this.etStreet = layout.findViewById(R.id.frAddressInfo_etStreet);
        this.etStreetChar = layout.findViewById(R.id.frAddressInfo_etStreetChar);
        this.etStreetNum = layout.findViewById(R.id.frAddressInfo_etStreetNum);
        this.spOrientation = layout.findViewById(R.id.frAddressInfo_spOrientation);
        this.spPosition=layout.findViewById(R.id.frAddressInfo_spPosition);
        this.btnNext=layout.findViewById(R.id.frAddressInfo_btnNext);
        this.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String street = etStreet.getText().toString();
                int streetNum = Integer.valueOf(etStreetNum.getText().toString());
                char streetChar = etStreetChar.getText().charAt(0);
                String state = etState.getText().toString();
                String city = etCity.getText().toString();
                String cadastralParticle= etCadastralParticle.getText().toString();
                Position position = positions.get((int) spPosition.getSelectedItemId());
                addressInformationInsertedListener.onAddressInformationInserted(street,streetNum,streetChar,city,state,cadastralParticle,position);
            }
        });

    }

    private void setUpSpinners() {
        List<String> positionTexts = new ArrayList<>();
        positions = DBHelper.getInstance(getActivity()).getAllPositions();
        for(Position position : positions){
            Log.d("POSITION TEXT SPINNER",position.getPosition());
            positionTexts.add(position.getPosition());
        }

        List<String> orientations = new ArrayList<>();


        ArrayAdapter<String> orientationAdapter = new ArrayAdapter<>(this.getActivity(),R.layout.support_simple_spinner_dropdown_item,orientations);
        orientationAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spOrientation.setAdapter(orientationAdapter);

        ArrayAdapter<String> positionAdapter = new ArrayAdapter<>(this.getActivity(),R.layout.support_simple_spinner_dropdown_item,positionTexts);

        positionAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spPosition.setAdapter(positionAdapter);


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof AddressInformationInserted)
        {
            this.addressInformationInsertedListener = (AddressInformationInserted) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.addressInformationInsertedListener =null;
    }

    public interface AddressInformationInserted{
        void onAddressInformationInserted(String street, int streetNum, char streetChar, String city,
                                          String state, String cadastralParticle,
                                          Position position);
    }


}
