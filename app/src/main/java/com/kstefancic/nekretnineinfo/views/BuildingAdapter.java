package com.kstefancic.nekretnineinfo.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.kstefancic.nekretnineinfo.BuildingDataActivity;
import com.kstefancic.nekretnineinfo.BuildingDetailsActivity;
import com.kstefancic.nekretnineinfo.LoginAndRegister.LoginActivity;
import com.kstefancic.nekretnineinfo.MainActivity;
import com.kstefancic.nekretnineinfo.R;
import com.kstefancic.nekretnineinfo.api.model.Building;
import com.kstefancic.nekretnineinfo.api.model.BuildingLocation;
import com.kstefancic.nekretnineinfo.api.model.localDBdto.LocalImage;
import com.kstefancic.nekretnineinfo.helper.DBHelper;
import com.kstefancic.nekretnineinfo.helper.RetrofitSingleton;
import com.kstefancic.nekretnineinfo.helper.SessionManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by user on 14.11.2017..
 */

public class BuildingAdapter extends RecyclerView.Adapter<ViewHolder> {

    public static final String BUILDING_INTENT = "building_details";
    public static final int UPDATE_BUILDING_RQST = 2;
    private static final String BUILDING = "building";

    private List<Building> buildings;
    private Context context;
    private SessionManager mSessionManager;

    public BuildingAdapter(List<Building> buildings, Context context, SessionManager sessionManager) {
        this.buildings = buildings;
        this.context = context;
        this.mSessionManager=sessionManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View buildingView =  LayoutInflater.from(context).inflate(R.layout.building_item, parent, false);
        return new ViewHolder(buildingView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Building building = this.buildings.get(position);

        setLocationdata(holder,building);

        setDateView(holder, building);

        setImageShiftButtons(holder, building);

        setEditButton(holder, building);

        setDeleteButton(holder,building);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailsIntent = new Intent(context, BuildingDetailsActivity.class);
                detailsIntent.putExtra(BUILDING_INTENT,building);
                context.startActivity(detailsIntent);
            }
        });

         /* List<String> imagePaths = new ArrayList<>();
                for(LocalImage localImage : images){
                    imagePaths.add(localImage.getImagePath());
                }
                uploadAlbum(imagePaths, position);*/

    }

    private void setDeleteButton(ViewHolder holder, final Building building) {
        holder.ibDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i<buildings.size();i++){
                    if(Objects.equals(buildings.get(i).getId(), building.getId())){
                        buildings.remove(i);
                        break;
                    }
                }
                notifyDataSetChanged();
                DBHelper.getInstance(context).deleteBuilding(building.getId());
            }
        });
    }

    private void setEditButton(ViewHolder holder, final Building building) {
        holder.ibEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent buildingIntent = new Intent(context,BuildingDataActivity.class);
                buildingIntent.putExtra(BUILDING, building);
                ((Activity) context).startActivityForResult(buildingIntent, UPDATE_BUILDING_RQST);
            }
        });
    }

    private void setDateView(ViewHolder holder, Building building) {
        if(building.isSynchronizedWithDatabase()){
            holder.tvDate.setBackground(context.getResources().getDrawable(R.drawable.cv_tv_synced));
        }else{
            holder.tvDate.setBackground(context.getResources().getDrawable(R.drawable.cv_tv_not_sync));

        }
        String date = new SimpleDateFormat("dd. MMM yyyy. HH:mm").format(building.getDate());
        holder.tvDate.setText(date);
    }

    @NonNull
    private List<LocalImage> getImages(Building building) {
        final List<LocalImage> images = DBHelper.getInstance(context).getImagesByBuildingId(building.getId());
        Log.d("IMAGES FROM BUILD", String.valueOf(images.size())+" "+String.valueOf(buildings.size()));
        for(int i=0; i<images.size();i++){
            images.get(i).setListId(i);
        }
        return images;
    }

    private void setImageShiftButtons(final ViewHolder holder, Building building) {
        final List<LocalImage> images = getImages(building);
        final LocalImage[] currentImage = {images.get(0)};
        holder.ivBuilding.setImageBitmap(currentImage[0].getImage());
        holder.ibNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = currentImage[0].getListId();
                if(index<images.size()-1){
                    holder.ivBuilding.setImageBitmap(images.get(++index).getImage());
                    currentImage[0] =images.get(index);
                }


            }
        });

        holder.ibPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = currentImage[0].getListId();
                if(index>0){
                    holder.ivBuilding.setImageBitmap(images.get(--index).getImage());
                    currentImage[0] =images.get(index);
                }

            }
        });
    }

    //TODO Write this in another way - better way!
    private void setLocationdata(ViewHolder holder, Building building) {
        List<BuildingLocation> locations = building.getLocations();
        Log.d("LOCATIONS", String.valueOf(locations.size())+" "+String.valueOf(buildings.size()));
        String streets ="";
        String streetNums = "";
        String cadastralParticles ="";
        for(int i=0; i<locations.size(); i++){
            BuildingLocation location = locations.get(i);
            if(i==0){
                streets = location.getStreet();
                streetNums = String.valueOf(location.getStreetNumber()+location.getStreetNumberChar());
                cadastralParticles = location.getCadastralParticle();
            }else{
                boolean streetExists = false, numExists = false, cadastralExists =false;
                for(int j=0;j<i;j++){
                    BuildingLocation existingLoc = locations.get(j);
                    if (location.getStreet().equals(existingLoc.getStreet())) {
                        streetExists=true;
                    }
                    if(String.valueOf(location.getStreetNumber()+location.getStreetNumberChar()).equals(String.valueOf(existingLoc.getStreetNumber()+existingLoc.getStreetNumberChar()))){
                        numExists = true;
                    }
                    if(location.getCadastralParticle().equals(existingLoc.getCadastralParticle())){
                        cadastralExists=true;
                    }
                }
                if(!streetExists){
                    streets+=", "+location.getStreet();
                }
                if(!numExists){
                    streetNums += ", "+location.getStreetNumber()+location.getStreetNumberChar();
                }
                if(!cadastralExists){
                    cadastralParticles+=", "+location.getCadastralParticle();
                }
            }
        }
        holder.tvStreetItems.setText(streets);
        holder.tvCity.setText(locations.get(0).getCity());
        holder.tvStreetNumItems.setText(streetNums);
        holder.tvCadastralParticles.setText(cadastralParticles);
    }

    @Override
    public int getItemCount() {
        return this.buildings.size();
    }


    private void uploadAlbum(List<String> imagePaths, final int position) {

        List<MultipartBody.Part> parts = new ArrayList<>();
        Log.d("fileURIs size",String.valueOf(imagePaths.size()));
        for(int i=0; i< imagePaths.size();i++){
            parts.add(prepareFilePart("files",imagePaths.get(i)));
        }
        Log.d("BEFORE UPLOAD",setAuthenticationHeader(position)+"\n"+buildings.get(position).getUser().toString()+"\n"+parts.size()+"\n"+buildings.get(position).toString());
        Call<ResponseBody> call = RetrofitSingleton.getBuildingService().uploadBuilding(setAuthenticationHeader(position),buildings.get(position).getUser().getUsername(),parts,buildings.get(position));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                buildings.get(position).setSynchronizedWithDatabase(true);
                notifyDataSetChanged();
                Log.d("GOOD",response.toString());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Error", t.toString());
            }
        });
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, String imagePath){
        Log.d("IMAGE PATH",imagePath);
        File imageFile = new File(imagePath);

        RequestBody requestFile = RequestBody.create(MediaType.parse("ivPicture/*"),imageFile);

        MultipartBody.Part part = MultipartBody.Part.createFormData(partName, imageFile.getName(), requestFile);
        Log.d("MULTIPARTBODY",part.body().contentType().toString());
        return part;
    }

    private String setAuthenticationHeader(int position) {
        String base = buildings.get(position).getUser().getUsername()+":"+this.mSessionManager.getPassword();
        return "Basic "+ Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);
    }
}


class ViewHolder extends RecyclerView.ViewHolder{

    TextView tvStreetItems, tvDate, tvStreetNumItems, tvCadastralParticles, tvCity;
    ImageView ivBuilding;
    ImageButton ibNext, ibPrevious, ibDelete, ibEdit;

    public ViewHolder(View itemView) {
        super(itemView);
        this.ibNext = itemView.findViewById(R.id.buildingItem_ibNext);
        this.ibPrevious = itemView.findViewById(R.id.buildingItem_ibPrevious);
        this.tvDate = itemView.findViewById(R.id.buildingItem_tvDate);
        this.ivBuilding = itemView.findViewById(R.id.buildingItem_ivBuilding);
        this.tvCadastralParticles = itemView.findViewById(R.id.buildingItem_tvCadastralPartItems);
        this.tvStreetNumItems = itemView.findViewById(R.id.buildingItem_tvStreetNumItems);
        this.tvStreetItems = itemView.findViewById(R.id.buildingItem_tvStreetItems);
        this.tvCity = itemView.findViewById(R.id.buildingItem_tvCityItem);
        this.ibDelete = itemView.findViewById(R.id.buildingItem_ibDelete);
        this.ibEdit = itemView.findViewById(R.id.buildingItem_ibEdit);
    }
}
