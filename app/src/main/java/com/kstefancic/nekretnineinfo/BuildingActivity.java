package com.kstefancic.nekretnineinfo;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.kstefancic.nekretnineinfo.api.model.Building;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.CeilingMaterial;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.ConstructionSystem;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.Material;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.Position;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.Purpose;
import com.kstefancic.nekretnineinfo.api.model.User;
import com.kstefancic.nekretnineinfo.buildinginsert.AddressInformationFragment;
import com.kstefancic.nekretnineinfo.buildinginsert.DimensionsFragment;
import com.kstefancic.nekretnineinfo.buildinginsert.OtherInformationFragment;
import com.kstefancic.nekretnineinfo.buildinginsert.PicturesFragment;
import com.kstefancic.nekretnineinfo.helper.DBHelper;
import com.kstefancic.nekretnineinfo.helper.SessionManager;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Random;

import static com.kstefancic.nekretnineinfo.LoginAndRegister.LoginActivity.USER;
import static com.kstefancic.nekretnineinfo.MainActivity.BUILDING_DATA;

public class BuildingActivity extends AppCompatActivity implements AddressInformationFragment.AddressInformationInserted,DimensionsFragment.DimensionsInserted,OtherInformationFragment.OtherInformationInserted, PicturesFragment.PictureChoosen{

    private static final String ADDRESS_INFO_FR = "address_info_fragment";

    private Building building;
    private User mUser;
    private SessionManager mSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building);
        this.mUser = (User) getIntent().getExtras().getSerializable(USER);
        building=new Building();
        Random random = new Random();
        long rndId = random.nextLong();
        building.setId(rndId);
        mSessionManager = new SessionManager(this);
        setUpFragment();

        if(ContextCompat.checkSelfPermission(BuildingActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(BuildingActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
        }
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
    public void onAddressInformationInserted(String street, int streetNum, char streetChar, String city, String state, String cadastralParticle, Position position) {
        /*building.setLocation(cadastralParticle,street,streetNum,streetChar,city,state,orientation,position);
        replaceFragment(new DimensionsFragment());*/
    }

    @Override
    public void onDimensionsInformationInserted(double length, double width, double brutoArea, double floorArea, double fullHeight, double floorHeight, int numOfFloors) {
       /* building.setDimensions(width,length,floorArea,brutoArea,floorHeight,fullHeight,numOfFloors);
        replaceFragment(new OtherInformationFragment());*/
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
        for(final Uri uri : fileUris){
            Picasso.with(this)
                    .load(uri)
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                            byte[] imageBytes = outputStream.toByteArray();
                            Log.d("INSERTING IMAGE", String.valueOf(building.getId()));
                            DBHelper.getInstance(getApplicationContext()).insertImage(getRealPathFromUri(uri),imageBytes,building.getId());

                            insertBuildingInLocalDatabase();

                            Intent returnIntent = new Intent();
                            returnIntent.putExtra(BUILDING_DATA, building);
                            setResult(RESULT_OK,returnIntent);
                            Log.d("RESULT_OK",building.toString());
                            finish();
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {
                            Log.d("FAIL INSERTING IMAGE",uri.toString());
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                            Log.d("PREPARE INSERTING IMAGE",uri.toString());
                        }
                    });
        }


        //uploadAlbum(fileUris);  - Upload na server
    }

    public String getRealPathFromUri(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = getContentResolver().query(contentUri, proj, null,
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

    private void insertBuildingInLocalDatabase() {

        building.setDate(new Timestamp(System.currentTimeMillis()));
        building.setYearOfBuild("1994.");
        building.setUser(mUser);
        building.setSynchronizedWithDatabase(false);
        DBHelper.getInstance(this).insertBuilding(building);
    }


}
