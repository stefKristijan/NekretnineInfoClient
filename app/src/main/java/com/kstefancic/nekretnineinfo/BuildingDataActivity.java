package com.kstefancic.nekretnineinfo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Picture;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.kstefancic.nekretnineinfo.api.model.Building;
import com.kstefancic.nekretnineinfo.api.model.BuildingLocation;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.CeilingMaterial;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.ConstructionSystem;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.Material;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.Position;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.Purpose;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.Roof;
import com.kstefancic.nekretnineinfo.api.model.localDBdto.LocalImage;
import com.kstefancic.nekretnineinfo.buildingdata.AddressInformationFragment;
import com.kstefancic.nekretnineinfo.buildingdata.BuildingDetailsFragment;
import com.kstefancic.nekretnineinfo.buildingdata.DimensionsFragment;
import com.kstefancic.nekretnineinfo.buildingdata.PicturesFragment;
import com.kstefancic.nekretnineinfo.helper.DBHelper;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.kstefancic.nekretnineinfo.MainActivity.BUILDING_DATA;

public class BuildingDataActivity extends AppCompatActivity implements View.OnClickListener,BuildingDetailsFragment.BuildingDetailsInserted, AddressInformationFragment.AddressInformationInserted, DimensionsFragment.DimensionsInserted,PicturesFragment.PictureChoosen{

    private static final String VALIDATE = "Potvrdite podatke radi validacije";
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private ImageButton ibCancel, ibSave;
    private int[] tabIcons={
          /*  R.drawable.ic_business_black_48dp,
            R.drawable.ic_place_black_48dp,
            R.drawable.ic_straighten_black_48dp,
            R.drawable.ic_photo_library_black_48dp*/
            R.mipmap.building,
            R.mipmap.location,
            R.mipmap.dimension,
            R.mipmap.gallery
    };

    private Building mBuilding;
    private List<LocalImage> images = new ArrayList<>();
    private boolean hasDimensions=false, hasLocations=false, hasDetails=false, hasPictures=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_data);



        mBuilding = new Building();
        mBuilding.setId(DBHelper.getInstance(this).getMaxId()+1);
        mBuilding.setuId(UUID.randomUUID().toString());
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.container);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        setUpViewPager();

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        setUpTabIcons();

       setUpButtons();

        if(ContextCompat.checkSelfPermission(BuildingDataActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(BuildingDataActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
        }
    }

    private void setUpButtons() {
        this.ibCancel = findViewById(R.id.main_ibCancel);
        this.ibCancel.setOnClickListener(this);
        this.ibSave = findViewById(R.id.main_ibSave);
        this.ibSave.setOnClickListener(this);
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
    private void setUpTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        Log.d("TAB ICON", String.valueOf(tabIcons[0]));
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        Log.d("TAB ICON", String.valueOf(tabIcons[1]));
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        Log.d("TAB ICON", String.valueOf(tabIcons[2]));
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        Log.d("TAB ICON", String.valueOf(tabIcons[3]));
    }

    private void setUpViewPager() {
        mSectionsPagerAdapter.addFragment(new BuildingDetailsFragment());
        mSectionsPagerAdapter.addFragment(new AddressInformationFragment());
        mSectionsPagerAdapter.addFragment(new DimensionsFragment());
        mSectionsPagerAdapter.addFragment(new PicturesFragment());
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_building_data, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDimensionsInformationInserted(double length, double width, double brutoArea, double basementArea, double residentalArea, double businessArea, double fullHeight, double floorHeight, int numOfFloors, boolean properGroundPlan) {
        this.mBuilding.setDimensions(width,length,brutoArea,floorHeight,fullHeight,numOfFloors,residentalArea,basementArea,businessArea);
        this.mBuilding.setProperGroundPlan(properGroundPlan);
        this.mViewPager.setCurrentItem(3,true);
        this.hasDimensions=true;

    }

    @Override
    public void onBuildingDetailsInserted(Material wallMaterial, CeilingMaterial ceilingMaterial, ConstructionSystem constructionSystem, Roof roof, Purpose purpose, String yearOfBuild) {
        this.mBuilding.setMaterial(wallMaterial);
        this.mBuilding.setCeilingMaterial(ceilingMaterial);
        this.mBuilding.setConstructionSystem(constructionSystem);
        this.mBuilding.setRoof(roof);
        this.mBuilding.setPurpose(purpose);
        this.mBuilding.setYearOfBuild(yearOfBuild);
        this.mViewPager.setCurrentItem(1,true);
        this.hasDetails=true;
    }

    @Override
    public void onAddressInformationInserted(List<BuildingLocation> buildingLocations, Position position) {
        this.mBuilding.setLocations(buildingLocations);
        this.mBuilding.setPosition(position);
        this.mViewPager.setCurrentItem(2,true);
        this.hasLocations=true;
    }

    @Override
    public void onPictureChoosenListener(final ArrayList<LocalImage> images) {
        this.images = images;
        hasPictures =true;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){

            case R.id.main_ibCancel:
                Intent returnIntent = new Intent();
                //returnIntent.putExtra(BUILDING_DATA, mBuilding);
                setResult(RESULT_CANCELED, returnIntent);
                Log.d("RESULT_CANCEL", "canceled");
                finish();
                break;

            case R.id.main_ibSave:
                if(dataValidationCheck()){
                    saveImages();
                    Intent saveIntent = new Intent();
                    saveIntent.putExtra(BUILDING_DATA, mBuilding);
                    setResult(RESULT_OK, saveIntent);
                    Log.d("RESULT_OK", mBuilding.toString());
                    finish();
                }
                break;
        }
    }

    private boolean dataValidationCheck() {

        if(!hasDetails){
            this.mViewPager.setCurrentItem(0,true);
            Toast.makeText(this,VALIDATE,Toast.LENGTH_SHORT).show();
            return false;
        }else if(!hasLocations){
            this.mViewPager.setCurrentItem(1,true);
            Toast.makeText(this,VALIDATE,Toast.LENGTH_SHORT).show();
            return false;
        }else if(!hasDimensions){
            this.mViewPager.setCurrentItem(2,true);
            Toast.makeText(this,VALIDATE,Toast.LENGTH_SHORT).show();
            return false;
        }else if(!hasPictures){
            this.mViewPager.setCurrentItem(3, true);
            Toast.makeText(this, VALIDATE, Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            return true;
        }
    }

    private void saveImages() {
        for (LocalImage localImage : images) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            localImage.getImage().compress(Bitmap.CompressFormat.JPEG, 70, outputStream);
            byte[] imageBytes = outputStream.toByteArray();
            localImage.setBuildingId(mBuilding.getId());
            DBHelper.getInstance(this).insertImage( imageBytes, mBuilding.getId());
        }

    }



    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragments = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
           return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }

        public void addFragment(Fragment fragment){
            this.fragments.add(fragment);
        }
        public Fragment getFragment(int position) {return this.fragments.get(position);}

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }
}
