package com.em2.kstefancic.nekretnineinfo;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.em2.kstefancic.nekretnineinfo.api.model.Building;
import com.em2.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.CeilingMaterial;
import com.em2.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.ConstructionSystem;
import com.em2.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.Material;
import com.em2.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.Position;
import com.em2.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.Purpose;
import com.em2.kstefancic.nekretnineinfo.api.model.User;
import com.em2.kstefancic.nekretnineinfo.api.service.BuildingService;
import com.em2.kstefancic.nekretnineinfo.buildinginsert.AddressInformationFragment;
import com.em2.kstefancic.nekretnineinfo.buildinginsert.DimensionsFragment;
import com.em2.kstefancic.nekretnineinfo.buildinginsert.OtherInformationFragment;
import com.em2.kstefancic.nekretnineinfo.buildinginsert.PicturesFragment;
import com.em2.kstefancic.nekretnineinfo.helper.SessionManager;

import java.io.File;
import java.sql.Timestamp;
import java.time.Instant;
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

import static com.em2.kstefancic.nekretnineinfo.LoginAndRegister.LoginActivity.BASE_URL;
import static com.em2.kstefancic.nekretnineinfo.LoginAndRegister.LoginActivity.USER;

public class BuildingActivity extends AppCompatActivity implements AddressInformationFragment.AddressInformationInserted,DimensionsFragment.DimensionsInserted,OtherInformationFragment.OtherInformationInserted, PicturesFragment.PictureChoosen{

    private static final String ADDRESS_INFO_FR = "address_info_fragment";

    private Retrofit mRetrofit;
    private Building building;
    private User mUser;
    private SessionManager mSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building);
        this.mUser = (User) getIntent().getExtras().getSerializable(USER);
        building=new Building();
        building.setDate(new Timestamp(System.currentTimeMillis()));
        building.setYearOfBuild("1994.");
        building.setUser(mUser);
        building.setId((long) 25);
        mSessionManager = new SessionManager(this);
        setUpFragment();

        if(ContextCompat.checkSelfPermission(BuildingActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(BuildingActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
        }
    }

    private void setRetrofit() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create());

        mRetrofit = builder.build();

    }

    private void setUpFragment() {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.activityBuilding_fl, new AddressInformationFragment(), ADDRESS_INFO_FR);
            fragmentTransaction.commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 100:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
        }
    }


    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null,
                    null, null);
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void uploadAlbum(ArrayList<Uri> fileUris) {
        setRetrofit();
        BuildingService buildingService = mRetrofit.create(BuildingService.class);

        List<MultipartBody.Part> parts = new ArrayList<>();
        Log.d("fileURIs size",String.valueOf(fileUris.size()));
        for(int i=0; i< fileUris.size();i++){
            parts.add(prepareFilePart("files",fileUris.get(i)));
        }
        Log.d("BEFORE UPLOAD",setAuthenticationHeader()+"\n"+mUser.toString()+"\n"+parts.size()+"\n"+building.toString());
        Call<ResponseBody> call = buildingService.uploadBuilding(setAuthenticationHeader(),mUser.getUsername(),parts,building);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("GOOD",response.toString());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Error", t.toString());
            }
        });
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri){
        String imagePath = getRealPathFromURI(this, fileUri);
        Log.d("IMAGE PATH",imagePath);
        File imageFile = new File(imagePath);

        RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(fileUri)),imageFile);

        MultipartBody.Part part = MultipartBody.Part.createFormData(partName, imageFile.getName(), requestFile);
        Log.d("MULTIPARTBODY",part.body().contentType().toString());
        return part;
    }

    private String setAuthenticationHeader() {
        String base = mUser.getUsername()+":"+this.mSessionManager.getPassword();
        return "Basic "+ Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.activityBuilding_fl, fragment);
        fragmentTransaction.commit();
    }


    /*
    FRAGMENT INTERFACES CALLBACKS
     */
    @Override
    public void onAddressInformationInserted(String street, int streetNum, char streetChar, String city, String state, String cadastralParticle, Building.Orientation orientation, Position position) {
        building.setLocation(cadastralParticle,street,streetNum,streetChar,city,state,orientation,position);
        replaceFragment(new DimensionsFragment());
    }

    @Override
    public void onDimensionsInformationInserted(double length, double width, double brutoArea, double floorArea, double fullHeight, double floorHeight, int numOfFloors) {
        building.setDimensions(width,length,floorArea,brutoArea,floorHeight,fullHeight,numOfFloors);
        replaceFragment(new OtherInformationFragment());
    }


    @Override
    public void onOtherInformationInserted(Material wallMaterial, CeilingMaterial ceilingMaterial, ConstructionSystem constructionSystem, Purpose purpose, boolean properGroundPlan) {
        building.setSynchronizedWithDatabase(false);
        building.setConstructionSystem(constructionSystem);
        building.setMaterial(wallMaterial);
        building.setCeilingMaterial(ceilingMaterial);
        building.setPurpose(purpose);
        replaceFragment(new PicturesFragment());
    }

    @Override
    public void onPictureChoosenListener(ArrayList<Uri> fileUris) {
        uploadAlbum(fileUris);
    }


}
