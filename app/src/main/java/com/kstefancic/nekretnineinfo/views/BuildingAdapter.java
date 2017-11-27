package com.kstefancic.nekretnineinfo.views;

import android.content.Context;
import android.graphics.Color;
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

import com.kstefancic.nekretnineinfo.R;
import com.kstefancic.nekretnineinfo.api.model.Building;
import com.kstefancic.nekretnineinfo.api.model.localDBdto.LocalImage;
import com.kstefancic.nekretnineinfo.api.service.BuildingService;
import com.kstefancic.nekretnineinfo.helper.DBHelper;
import com.kstefancic.nekretnineinfo.helper.SessionManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static com.kstefancic.nekretnineinfo.LoginAndRegister.LoginActivity.BASE_URL;

/**
 * Created by user on 14.11.2017..
 */

public class BuildingAdapter extends RecyclerView.Adapter<ViewHolder> {

    private List<Building> buildings;
    private Context context;
    private Retrofit mRetrofit;
    private SessionManager mSessionManager;

    public BuildingAdapter(List<Building> buildings, Context context, SessionManager sessionManager) {
        this.buildings = buildings;
        this.context = context;
        this.mSessionManager=sessionManager;
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
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Building building = this.buildings.get(position);

        final List<LocalImage> images = DBHelper.getInstance(context).getImagesByBuildingId(building.getId());
        Log.d("IMAGES FROM BUILD", String.valueOf(images.size())+" "+String.valueOf(buildings.size()));
        holder.ivBuilding.setImageBitmap(images.get(0).getImage());


        holder.tvLocation.setText(building.getLocations().get(0).getStreet()+" "+building.getLocations().get(0).getStreetNumber()+building.getLocations().get(0).getStreetNumberChar()+", "+building.getLocations().get(0).getCity());

        if(building.isSynchronizedWithDatabase()){
            holder.tvSynchronized.setText("Sinkronizirano");
            holder.tvSynchronized.setTextColor(Color.GREEN);
        }else{
            holder.tvSynchronized.setText("Nije sinkronizirano");
            holder.tvSynchronized.setTextColor(Color.RED);
        }

        String date = new SimpleDateFormat("dd. MMM yyyy. HH:mm").format(building.getDate());
        holder.tvDate.setText(date);

        holder.ibSynchronize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> imagePaths = new ArrayList<>();
                for(LocalImage localImage : images){
                    imagePaths.add(localImage.getImagePath());
                }
                uploadAlbum(imagePaths, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.buildings.size();
    }

    private void setRetrofit() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create());

        mRetrofit = builder.build();

    }

    private void uploadAlbum(List<String> imagePaths, final int position) {
        setRetrofit();
        final BuildingService buildingService = mRetrofit.create(BuildingService.class);

        List<MultipartBody.Part> parts = new ArrayList<>();
        Log.d("fileURIs size",String.valueOf(imagePaths.size()));
        for(int i=0; i< imagePaths.size();i++){
            parts.add(prepareFilePart("files",imagePaths.get(i)));
        }
        Log.d("BEFORE UPLOAD",setAuthenticationHeader(position)+"\n"+buildings.get(position).getUser().toString()+"\n"+parts.size()+"\n"+buildings.get(position).toString());
        Call<ResponseBody> call = buildingService.uploadBuilding(setAuthenticationHeader(position),buildings.get(position).getUser().getUsername(),parts,buildings.get(position));

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

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"),imageFile);

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
