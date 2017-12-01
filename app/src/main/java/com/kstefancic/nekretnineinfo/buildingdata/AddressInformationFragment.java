package com.kstefancic.nekretnineinfo.buildingdata;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.kstefancic.nekretnineinfo.R;
import com.kstefancic.nekretnineinfo.api.model.BuildingLocation;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.Position;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.addressMultichoiceData.City;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.addressMultichoiceData.State;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.addressMultichoiceData.Street;
import com.kstefancic.nekretnineinfo.helper.DBHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by user on 15.11.2017..
 */

public class AddressInformationFragment extends Fragment {

    private static final String FORMAT_NOT_VALID = "Dopušteni formati katastarske čestice su: # ili #/#";
    private static final String EMPTY_FIELD = "*Obavezno polje";
    private static final String CHAR_ONLY = "Dopušteno jedno slovo";
    private static final String UNKNOWN_STREET = "Ulica ne postoji na popisu ulica";
    private static final String UNKNOWN_CITY= "Grad ne postoji na popisu gradova";
    private static final String UNKNOWN_STATE= "Županija ne postoji na popisu županija";
    private static final String LOCATION_EXISTS = "Lokacija već postoji";

    private Button btnAdd, btnAccept;
    private AutoCompleteTextView actvStreet, actvCity, actvState;
    private EditText etStreetNum, etStreetChar, etCadastralParticle;
    private ListView lvLocations;
    private Spinner spPosition;
    private TextInputLayout tilStreet, tilCity, tilState, tilCadastralParticle, tilStreetChar,tilStreetNum;
    private List<Position> positions;
    private List<State> states;
    private List<City> cities;
    private List<Street> streets;
    private List<BuildingLocation> buildingLocations = new ArrayList<>();
    ArrayList<String> locationListItems =new ArrayList<String>();
    ArrayAdapter<String> listViewAdapter;
    private AddressInformationInserted addressInformationInsertedListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("ADDRESSFRAGMENT","onCreate()");

        states = DBHelper.getInstance(getActivity()).getAllStates();
        cities = DBHelper.getInstance(getActivity()).getAllCities();
        streets = DBHelper.getInstance(getActivity()).getAllStreets();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_adress_information,null);
        Log.i("ADDRESSFRAGMENT","onCreateView()");

        setUI(layout);
        setUpSpinners();
        return layout;
    }

    private void setUI(View layout) {
        this.lvLocations = layout.findViewById(R.id.frAddressInfo_lvAddresses);
        this.listViewAdapter=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, locationListItems);
        this.lvLocations.setAdapter(listViewAdapter);
        this.tilCadastralParticle = layout.findViewById(R.id.frAddressInfo_tilCadastralParticle);
        this.tilState = layout.findViewById(R.id.frAddressInfo_tilState);
        this.tilStreet = layout.findViewById(R.id.frAddressInfo_tilStreet);
        this.tilCity = layout.findViewById(R.id.frAddressInfo_tilCity);
        this.tilStreetChar = layout.findViewById(R.id.frAddressInfo_tilChar);
        this.tilStreetNum=layout.findViewById(R.id.frAddressInfo_tilNumber);
        this.actvStreet = layout.findViewById(R.id.frAddressInfo_actvStreet);
        this.actvCity = layout.findViewById(R.id.frAddressInfo_actvCity);
        this.actvState = layout.findViewById(R.id.frAddressInfo_actvState);
        setUpAutoCompleteTextViews();
        this.etCadastralParticle = layout.findViewById(R.id.frAddressInfo_etCadastralParticle);
        this.etStreetChar = layout.findViewById(R.id.frAddressInfo_etStreetChar);
        this.etStreetNum = layout.findViewById(R.id.frAddressInfo_etStreetNum);
        this.spPosition=layout.findViewById(R.id.frAddressInfo_spPosition);
        this.btnAdd =layout.findViewById(R.id.frAddressInfo_btnAdd);
        this.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String street = actvStreet.getText().toString();
                String streetChar = etStreetChar.getText().toString();
                String state = actvState.getText().toString();
                String city = actvCity.getText().toString();
                String cadastralParticle= etCadastralParticle.getText().toString();


                if(checkData(street,streetChar,state, city, cadastralParticle)) {

                    int streetNum = Integer.valueOf(etStreetNum.getText().toString());
                    char streetCharacter = '\0';
                    if (!streetChar.isEmpty()) {
                        streetCharacter = streetChar.charAt(0);
                    }
                    BuildingLocation buildingLocation = new BuildingLocation(street, streetNum, streetCharacter, city, state, cadastralParticle);
                    if (!locationExists(buildingLocation)) {
                        locationListItems.add(buildingLocation.toString());
                        buildingLocations.add(buildingLocation);
                        Log.i("LOCATION", buildingLocation.toString() + " " + locationListItems.size());
                        listViewAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        this.btnAccept = layout.findViewById(R.id.frAddressInfo_btnNext);
        this.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("ADDRESS","onInsert");
                Position position = positions.get((int) spPosition.getSelectedItemId());
                addressInformationInsertedListener.onAddressInformationInserted(buildingLocations,position);
            }
        });

    }

    private boolean locationExists(BuildingLocation buildingLocation) {
        boolean exists=false;
        for(BuildingLocation buildingLocation1 : buildingLocations){
            if(buildingLocation1.equals(buildingLocation)){
                Toast.makeText(getActivity(),LOCATION_EXISTS, Toast.LENGTH_SHORT).show();
                exists=true;
                break;
            }
        }
        return exists;
    }

    private boolean checkData(String street, String streetChar, String state, String city, String cadastralParticle) {
        boolean isValid=true;
        refreshTilErrors();
        if(street.isEmpty()){
            isValid = false;
            tilStreet.setError(EMPTY_FIELD);
        }
        else if(!getStreetNames().contains(street)){
            isValid=false;
            tilStreet.setError(UNKNOWN_STREET);
        }

        if(state.isEmpty()){
            isValid=false;
            tilState.setError(EMPTY_FIELD);
        }
        else if(!getStateNames().contains(state)){
            isValid=false;
            tilState.setError(UNKNOWN_STATE);
        }

        if(city.isEmpty()){
            isValid=false;
            tilCity.setError(EMPTY_FIELD);
        }
        else if(!getCityNames().contains(city)){
            isValid=false;
            tilCity.setError(UNKNOWN_CITY);
        }

        if(streetChar.length()>1){
            isValid=false;
            tilStreetChar.setError(CHAR_ONLY);
        }

        if(cadastralParticle.isEmpty()){
            isValid = false;
            tilCadastralParticle.setError(EMPTY_FIELD);
        }
        else if(!Pattern.matches("^[0-9]{1,6}$|^[0-9]{1,6}\\/[0-9]{1,3}$",cadastralParticle)){
            isValid=false;
            tilCadastralParticle.setError(FORMAT_NOT_VALID);
        }

        if(etStreetNum.getText().toString().isEmpty()){
            isValid=false;
            tilStreetNum.setError("*");
        }
        return isValid;
    }

    private void refreshTilErrors() {
        tilStreet.setErrorEnabled(false);
        tilStreetNum.setErrorEnabled(false);
        tilCity.setErrorEnabled(false);
        tilState.setErrorEnabled(false);
        tilCadastralParticle.setErrorEnabled(false);
        tilStreetChar.setErrorEnabled(false);
    }

    private void setUpAutoCompleteTextViews() {
        setUpStreetAutocomplete();
        setUpCityAutocomplete();
        setUpStateAutocomplete();
    }

    private void setUpStateAutocomplete() {

        List<String> stateNames = getStateNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, stateNames);
        actvState.setAdapter(adapter);
    }

    @NonNull
    private List<String> getStateNames() {
        List<String> stateNames = new ArrayList<>();
        for(State state : states){
            stateNames.add(state.getStateName());
        }
        return stateNames;
    }

    private void setUpCityAutocomplete() {
        List<String> cityNames = getCityNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, cityNames);
        actvCity.setAdapter(adapter);
    }

    @NonNull
    private List<String> getCityNames() {
        List<String> cityNames = new ArrayList<>();
        for(City city : cities){
            cityNames.add(city.getCityName());
        }
        return cityNames;
    }

    private void setUpStreetAutocomplete() {
        List<String> streetNames = getStreetNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, streetNames);
        actvStreet.setAdapter(adapter);
    }

    @NonNull
    private List<String> getStreetNames() {
        List<String> streetNames = new ArrayList<>();
        for(Street street : streets){
            streetNames.add(street.getStreetName());
        }
        return streetNames;
    }

    private void setUpSpinners() {
        List<String> positionTexts = new ArrayList<>();
        positions = DBHelper.getInstance(getActivity()).getAllPositions();
        for(Position position : positions){
            Log.d("POSITION TEXT SPINNER",position.getPosition());
            positionTexts.add(position.getPosition());
        }


        ArrayAdapter<String> positionAdapter = new ArrayAdapter<>(this.getActivity(),R.layout.support_simple_spinner_dropdown_item,positionTexts);

        positionAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spPosition.setAdapter(positionAdapter);


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i("ADDRESSFRAGMENT","onAtach()");
        if(context instanceof AddressInformationInserted)
        {
            this.addressInformationInsertedListener = (AddressInformationInserted) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.addressInformationInsertedListener =null;
        Log.i("ADDRESSFRAGMENT","onDetach()");
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("ADDRESSFRAGMENT","onViewCreated()");
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.i("ADDRESSFRAGMENT","onViewStateRestored()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("ADDRESSFRAGMENT","onResume()");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("ADDRESSFRAGMENT","onSaveInstanceState()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("ADDRESSFRAGMENT","onPause()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("ADDRESSFRAGMENT","onDestroyView()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("ADDRESSFRAGMENT","onDestroy()");
    }

    public interface AddressInformationInserted{
        void onAddressInformationInserted(List<BuildingLocation> buildingLocations, Position position);
    }


}
