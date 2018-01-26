package com.kstefancic.potresnirizik.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kstefancic.potresnirizik.R;
import com.kstefancic.potresnirizik.api.model.BuildingLocation;

import java.util.List;

/**
 * Created by user on 8.12.2017..
 */

public class LocationAdapter extends ArrayAdapter<BuildingLocation>{

    private TextView tvAddress, tvCadastralParticle, tvCadastralMunicipality;

    public LocationAdapter(@NonNull Context context, int resource, @NonNull List<BuildingLocation> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view==null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view=inflater.inflate(R.layout.location_item, null);
        }

        BuildingLocation buildingLocation = getItem(position);

        if(buildingLocation!=null){
            String address = buildingLocation.getStreet()+" "+buildingLocation.getStreetNumber()+buildingLocation.getStreetChar()+", "+buildingLocation.getCity()+", "+buildingLocation.getState();
            this.tvAddress = view.findViewById(R.id.locationItem_tvAddressItem);
            this.tvCadastralParticle = view.findViewById(R.id.locationItem_tvCPitem);
            this.tvCadastralMunicipality= view.findViewById(R.id.locationItem_tvCMunicipalityitem);
            this.tvAddress.setText(address);
            this.tvCadastralParticle.setText(buildingLocation.getCadastralParticle());
            this.tvCadastralMunicipality.setText(buildingLocation.getCadastralMunicipality());
        }

        return view;
    }
}
