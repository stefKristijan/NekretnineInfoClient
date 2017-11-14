package com.em2.kstefancic.nekretnineinfo.views;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.em2.kstefancic.nekretnineinfo.LoginAndRegister.LoginActivity;
import com.em2.kstefancic.nekretnineinfo.R;
import com.em2.kstefancic.nekretnineinfo.api.model.Building;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by user on 14.11.2017..
 */

public class BuildingAdapter extends RecyclerView.Adapter<ViewHolder> {

    private List<Building> buildings;
    private Context context;

    public BuildingAdapter(List<Building> buildings, Context context) {
        this.buildings = buildings;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View buildingView = inflater.inflate(R.layout.building_item, parent, false);
        ViewHolder buildingViewHolder = new ViewHolder(buildingView);
        return buildingViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Building building = this.buildings.get(position);

        Log.d("IMAGE PATH",LoginActivity.BASE_URL+building.getImagePaths().get(0).getImagePath());

        Picasso.with(context)
                .load(LoginActivity.BASE_URL+building.getImagePaths().get(0).getImagePath())
                .fit()
                .centerCrop()
                .into(holder.ivBuilding);

        holder.tvLocation.setText(building.getStreet()+" "+building.getStreetNumber()+building.getStreetNumberChar()+", "+building.getCity());

        if(building.isSynchronizedWithDatabase()){
            holder.tvSynchronized.setText("Sinkronizirano");
            holder.tvSynchronized.setTextColor(Color.GREEN);
        }else{
            holder.tvSynchronized.setText("Nije sinkronizirano");
            holder.tvSynchronized.setTextColor(Color.RED);
        }

        String date = new SimpleDateFormat("dd. MMM yyyy. HH:mm").format(building.getDate());
        holder.tvDate.setText(date);

    }

    @Override
    public int getItemCount() {
        return this.buildings.size();
    }
}


class ViewHolder extends RecyclerView.ViewHolder{

    TextView tvLocation, tvDate, tvSynchronized;
    ImageButton ibSynchronize, ibEdit;
    ImageView ivBuilding;

    public ViewHolder(View itemView) {
        super(itemView);
        this.tvDate = itemView.findViewById(R.id.rvBuilding_tvDate);
        this.tvLocation = itemView.findViewById(R.id.rvBuilding_tvLocation);
        this.tvSynchronized = itemView.findViewById(R.id.rvBuilding_tvSynchronized);
        this.ibEdit = itemView.findViewById(R.id.rvBuilding_ibEdit);
        this.ibSynchronize = itemView.findViewById(R.id.rvBuilding_ibSynchronize);
        this.ivBuilding = itemView.findViewById(R.id.rvBuilding_ivBuilding);
    }
}
