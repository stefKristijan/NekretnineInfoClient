package com.kstefancic.nekretnineinfo.buildingdata;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.kstefancic.nekretnineinfo.R;
import com.kstefancic.nekretnineinfo.api.model.Building;
import com.kstefancic.nekretnineinfo.api.model.BuildingLocation;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.Position;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.addressMultichoiceData.City;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.addressMultichoiceData.State;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.addressMultichoiceData.Street;
import com.kstefancic.nekretnineinfo.helper.DBHelper;
import com.kstefancic.nekretnineinfo.views.LocationAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.kstefancic.nekretnineinfo.MainActivity.BUILDING_DATA;

/**
 * Created by user on 15.11.2017..
 */

public class AddressInformationFragment extends Fragment {

    private static final String FORMAT_NOT_VALID = "Dopušteni formati katastarske čestice i općine su: # ili #/#";
    private static final String EMPTY_FIELD = "*Obavezno polje";
    private static final String CHAR_ONLY = "Dopušteno jedno slovo";
    private static final String UNKNOWN_STREET = "Ulica ne postoji na popisu ulica";
    private static final String UNKNOWN_CITY= "Grad ne postoji na popisu gradova";
    private static final String UNKNOWN_STATE= "Županija ne postoji na popisu županija";
    private static final String LOCATION_EXISTS = "Lokacija već postoji";

    private Button btnAdd, btnAccept;
    private AutoCompleteTextView actvStreet, actvCity, actvSettlement;
    private EditText etStreetNum, etStreetChar, etCadastralParticle, etCityDistrict, etCadastralMunicipality;
    private ListView lvLocations;
    private Spinner spPosition;
    private TextInputLayout tilStreet, tilCity, tilCadastralParticle, tilCadastralMunicipality, tilCityDistrict, tilSettlement, tilStreetChar,tilStreetNum;
    private List<Position> positions;
    private List<State> states;
    private List<City> cities;
    private List<Street> streets;
    //private List<Settlement> settlements /popis naselja
    private List<BuildingLocation> buildingLocations = new ArrayList<>();
    private Building mBuilding;
    private LocationAdapter listViewAdapter;
    private AddressInformationInserted addressInformationInsertedListener;

    public static AddressInformationFragment newInstance(Building building) {
        AddressInformationFragment fragment = new AddressInformationFragment();
        Bundle bundle = new Bundle();

        bundle.putSerializable(BUILDING_DATA, building);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        states = DBHelper.getInstance(getActivity()).getAllStates();
        cities = DBHelper.getInstance(getActivity()).getAllCities();
        streets = DBHelper.getInstance(getActivity()).getAllStreets();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_adress_information,null);
        mBuilding = (Building) getArguments().getSerializable(BUILDING_DATA);
        if(mBuilding!=null){
            buildingLocations = mBuilding.getLocations();
        }
        setUI(layout);
        setUpSpinners();
        return layout;
    }

    private void setUI(View layout) {
        this.lvLocations = layout.findViewById(R.id.frAddressInfo_lvAddresses);
        this.listViewAdapter=new LocationAdapter(getActivity(), R.layout.location_item, buildingLocations);
        this.lvLocations.setAdapter(listViewAdapter);
        this.tilCadastralParticle = layout.findViewById(R.id.frAddressInfo_tilCadastralParticle);
        this.tilStreet = layout.findViewById(R.id.frAddressInfo_tilStreet);
        this.tilSettlement = layout.findViewById(R.id.frAddressInfo_tilSettlement);
        this.tilCityDistrict=layout.findViewById(R.id.frAddressInfo_tilcityDistrict);
        this.tilCadastralMunicipality = layout.findViewById(R.id.frAddressInfo_tilCadastralMunicipality);
        this.tilCity = layout.findViewById(R.id.frAddressInfo_tilCity);
        this.tilStreetChar = layout.findViewById(R.id.frAddressInfo_tilChar);
        this.tilStreetNum=layout.findViewById(R.id.frAddressInfo_tilNumber);
        this.actvStreet = layout.findViewById(R.id.frAddressInfo_actvStreet);
        this.actvCity = layout.findViewById(R.id.frAddressInfo_actvCity);
        this.actvSettlement = layout.findViewById(R.id.frAddressInfo_actvSettlement);
        setUpAutoCompleteTextViews();
        this.etCadastralParticle = layout.findViewById(R.id.frAddressInfo_etCadastralParticle);
        this.etStreetChar = layout.findViewById(R.id.frAddressInfo_etStreetChar);
        this.etStreetNum = layout.findViewById(R.id.frAddressInfo_etStreetNum);
        this.etCadastralMunicipality = layout.findViewById(R.id.frAddressInfo_etCadastralMunicipality);
        this.etCityDistrict = layout.findViewById(R.id.frAddressInfo_etCityDistrict);
        this.spPosition=layout.findViewById(R.id.frAddressInfo_spPosition);
        this.btnAdd =layout.findViewById(R.id.frAddressInfo_btnAdd);
        this.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String street = actvStreet.getText().toString().trim();
                String streetChar = etStreetChar.getText().toString().trim();
                String city = actvCity.getText().toString().trim();
                String cadastralParticle= etCadastralParticle.getText().toString().trim();
                String settlement = actvSettlement.getText().toString().trim();
                String cadastralMunicipality = etCadastralMunicipality.getText().toString().trim();
                String cityDistrict = etCityDistrict.getText().toString().trim();

                if(checkData(street,streetChar, city, cadastralParticle, settlement, cadastralMunicipality, cityDistrict)) {

                    String state = getState(city);

                    int streetNum = Integer.valueOf(etStreetNum.getText().toString());
                    char streetCharacter = '\0';
                    if (!streetChar.isEmpty()) {
                        streetCharacter = streetChar.charAt(0);
                    }
                    BuildingLocation buildingLocation = new BuildingLocation(street, streetNum, streetCharacter, settlement, cityDistrict, city, state, cadastralParticle, cadastralMunicipality);
                    if (!locationExists(buildingLocation)) {
                        buildingLocations.add(buildingLocation);
                        listViewAdapter.notifyDataSetChanged();
                        etCadastralParticle.setText("");
                        etStreetChar.setText("");
                        etStreetNum.setText("");
                        actvStreet.setText("");
                        actvCity.setText("");
                        actvSettlement.setText("");
                        etCadastralMunicipality.setText("");
                        etCityDistrict.setText("");
                    }
                }
            }
        });

        this.btnAccept = layout.findViewById(R.id.frAddressInfo_btnNext);
        this.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(buildingLocations.size()>0) {
                    Position position = positions.get((int) spPosition.getSelectedItemId());
                    addressInformationInsertedListener.onAddressInformationInserted(buildingLocations, position);
                }else{
                    Toast.makeText(getActivity(),"Morate dodati barem jednu lokaciju", Toast.LENGTH_SHORT).show();
                }
            }
        });

        this.lvLocations.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                dialogBuilder.setTitle("Brisanje")
                        .setMessage("Jeste li sigurni da želite obrisati lokaciju?")
                        .setNegativeButton("Odustani", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton("Obriši", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                buildingLocations.remove(i);
                                listViewAdapter.notifyDataSetChanged();
                            }
                        })
                        .show();
                return false;
            }
        });
        this.lvLocations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                BuildingLocation buildingLocation = buildingLocations.get(i);
                etCadastralParticle.setText(buildingLocation.getCadastralParticle());
                etCadastralMunicipality.setText(buildingLocation.getCadastralMunicipality());
                etCityDistrict.setText(buildingLocation.getCityDistrict());
                if(buildingLocation.getStreetChar()!='\0')
                    etStreetChar.setText(buildingLocation.getStreetChar());
                etStreetNum.setText(String.valueOf(buildingLocation.getStreetNumber()));
                actvCity.setText(buildingLocation.getCity());
                actvSettlement.setText(buildingLocation.getSettlement());
                actvStreet.setText(buildingLocation.getStreet());
            }
        });
    }

    private String getState(String city) {
        String state ="";
        for(City city1:cities){
            if(city1.getCityName().equals(city)){
                for(State state1:states){
                    if(state1.getId()==city1.getStateId()){
                        state=state1.getStateName();
                        break;
                    }
                }
                break;
            }
        }
        return state;
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

    private boolean checkData(String street, String streetChar,  String city, String cadastralParticle, String settlement, String cadastralMunicipality, String cityDistrict) {
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
        }else if(streetChar.length()!=0 && Character.isDigit(streetChar.charAt(0))){

            isValid=false;
            tilStreetChar.setError(CHAR_ONLY);
        }

        if(settlement.isEmpty()){
            isValid=false;
            tilSettlement.setError(EMPTY_FIELD);
        }

        if(cadastralParticle.isEmpty()){
            isValid = false;
            tilCadastralParticle.setError(EMPTY_FIELD);
        }
        else if(!Pattern.matches("^[0-9]{1,6}$|^[0-9]{1,6}\\/[0-9]{1,3}$",cadastralParticle)){
            isValid=false;
            tilCadastralParticle.setError(FORMAT_NOT_VALID);
        }

        if(cadastralMunicipality.isEmpty()){
            isValid = false;
            tilCadastralMunicipality.setError(EMPTY_FIELD);
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
        tilCadastralMunicipality.setErrorEnabled(false);
        tilCityDistrict.setErrorEnabled(false);
        tilSettlement.setErrorEnabled(false);
        tilCadastralParticle.setErrorEnabled(false);
        tilStreetChar.setErrorEnabled(false);
    }

    private void setUpAutoCompleteTextViews() {
        setUpStreetAutocomplete();
        setUpCityAutocomplete();
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
            positionTexts.add(position.getPosition());
        }


        ArrayAdapter<String> positionAdapter = new ArrayAdapter<>(this.getActivity(),R.layout.spinner_item,positionTexts);

        positionAdapter.setDropDownViewResource(R.layout.spinner_item);
        spPosition.setAdapter(positionAdapter);


        if(mBuilding!=null){
            for(int i=0; i<positionTexts.size(); i++){
                if(positionTexts.get(i).equals(mBuilding.getPosition().getPosition())){
                    spPosition.setSelection(i);
                    break;
                }
            }
        }

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
        void onAddressInformationInserted(List<BuildingLocation> buildingLocations, Position position);
    }


}
