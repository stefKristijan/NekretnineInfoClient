package com.kstefancic.potresnirizik;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.kstefancic.potresnirizik.api.model.Building;
import com.kstefancic.potresnirizik.api.model.BuildingLocation;
import com.kstefancic.potresnirizik.api.model.MultiChoiceModels.CeilingMaterial;
import com.kstefancic.potresnirizik.api.model.MultiChoiceModels.Construction;
import com.kstefancic.potresnirizik.api.model.MultiChoiceModels.Material;
import com.kstefancic.potresnirizik.api.model.MultiChoiceModels.Position;
import com.kstefancic.potresnirizik.api.model.MultiChoiceModels.Purpose;
import com.kstefancic.potresnirizik.api.model.MultiChoiceModels.Roof;
import com.kstefancic.potresnirizik.api.model.localDBdto.LocalImage;
import com.kstefancic.potresnirizik.buildingdata.AddressInformationFragment;
import com.kstefancic.potresnirizik.buildingdata.BuildingDetailsFragment;
import com.kstefancic.potresnirizik.buildingdata.DimensionsFragment;
import com.kstefancic.potresnirizik.buildingdata.PicturesFragment;
import com.kstefancic.potresnirizik.helper.DBHelper;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.kstefancic.potresnirizik.MainActivity.BUILDING_DATA;

public class BuildingDataActivity extends AppCompatActivity implements View.OnClickListener,BuildingDetailsFragment.BuildingDetailsInserted, AddressInformationFragment.AddressInformationInserted, DimensionsFragment.DimensionsInserted,PicturesFragment.PictureChoosen{

    private static final String VALIDATE = "Potvrdite podatke radi validacije";
    private static final String NO_CONSTRUCTION_SYSTEM = "Morate odabrati konstrukcijski sustav kako bi spremili dimenzije";
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
    private ImageButton btnCancel, btnSave;
    private int[] tabIcons={
            R.drawable.ic_place_white_24dp,
            R.drawable.ic_business_white_24dp,
            R.drawable.ic_straighten_white_24dp,
            R.drawable.ic_photo_library_white_24dp
    };

    private Building mBuilding;
    private List<LocalImage> images = new ArrayList<>();
    private boolean hasDimensions=false, hasLocations=false, hasDetails=false, hasPictures=false, isUpdating=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_data);

        this.mBuilding = (Building) getIntent().getSerializableExtra(BUILDING_DATA);
        setActivity();

        if(ContextCompat.checkSelfPermission(BuildingDataActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(BuildingDataActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
        }
        if(ContextCompat.checkSelfPermission(BuildingDataActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(BuildingDataActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},101);
        }
    }

    private void setActivity() {

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.container);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        setUpViewPager();

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        setUpTabIcons();

        setUpButtons();

        if(mBuilding==null){
            mBuilding = new Building();
            mBuilding.setId(DBHelper.getInstance(this).getMaxId()+1);
            mBuilding.setuId(UUID.randomUUID().toString());
        }else{
            isUpdating=true;
        }
    }

    private void setUpButtons() {
        this.btnCancel = findViewById(R.id.main_btnCancel);
        this.btnCancel.setOnClickListener(this);
        this.btnSave = findViewById(R.id.main_btnSave);
        this.btnSave.setOnClickListener(this);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 100:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;

            case 101:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
        }
    }
    private void setUpTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
    }

    private void setUpViewPager() {
        Fragment addressFragment = AddressInformationFragment.newInstance(mBuilding);
        Fragment detailsFragment = BuildingDetailsFragment.newInstance(mBuilding);
        Fragment dimensionFragment = DimensionsFragment.newInstance(mBuilding);
        Fragment picturesFragment = PicturesFragment.newInstance(mBuilding);
        mSectionsPagerAdapter.addFragment(addressFragment);
        mSectionsPagerAdapter.addFragment(detailsFragment);
        mSectionsPagerAdapter.addFragment(dimensionFragment);
        mSectionsPagerAdapter.addFragment(picturesFragment);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }



    @Override
    public void onDimensionsInformationInserted(double length, double width, double brutoArea, double basementArea, double residentalArea, double businessArea, double fullHeight, double floorHeight, int numOfFloors, int numOfFlats, boolean properGroundPlan) {
        this.mBuilding.setDimensions(width,length,brutoArea,floorHeight,fullHeight,numOfFloors,numOfFlats,residentalArea,basementArea,businessArea);
        this.mBuilding.setProperGroundPlan(properGroundPlan);
        if(numOfFlats>0){
            this.mBuilding.setNumberOfResidents((int) (numOfFlats*2.56));
        }else{
            this.mBuilding.setNumberOfResidents(0);
        }
        if(mBuilding.getConstruction()==null){
            Toast.makeText(this, NO_CONSTRUCTION_SYSTEM, Toast.LENGTH_SHORT).show();
            this.mViewPager.setCurrentItem(1,true);
            this.hasDimensions = false;
        }else {
            this.mBuilding.setNetoArea(calculateNetoArea(brutoArea, numOfFloors));
            this.mViewPager.setCurrentItem(3, true);
            this.hasDimensions = true;
        }
    }

    private double calculateNetoArea(double brutoArea, int numofFloors) {
        /*
        With switch case(constructionSystem) AB - 0.83, zidane - 0.75
         */
        return brutoArea*numofFloors*0.8;
    }

    @Override
    public void onBuildingDetailsInserted(Material wallMaterial, CeilingMaterial ceilingMaterial, Construction construction, Roof roof, Purpose purpose, String yearOfBuild, String companyInBuilding, String maintenanceGrade) {
        this.mBuilding.setMaterial(wallMaterial);
        this.mBuilding.setCeilingMaterial(ceilingMaterial);
        this.mBuilding.setConstruction(construction);
        this.mBuilding.setRoof(roof);
        this.mBuilding.setPurpose(purpose);
        this.mBuilding.setYearOfBuild(yearOfBuild);
        this.mBuilding.setMaintenanceGrade(maintenanceGrade);
        this.mBuilding.setCompanyInBuilding(companyInBuilding);
        this.mViewPager.setCurrentItem(2,true);
        this.hasDetails=true;
    }

    @Override
    public void onAddressInformationInserted(List<BuildingLocation> buildingLocations, Position position) {
        this.mBuilding.setLocations(buildingLocations);
        this.mBuilding.setPosition(position);
        this.mViewPager.setCurrentItem(1,true);
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

            case R.id.main_btnCancel:
                Intent returnIntent = new Intent();
                //returnIntent.putExtra(BUILDING_DATA, mBuilding);
                setResult(RESULT_CANCELED, returnIntent);
                Log.d("RESULT_CANCEL", "canceled");
                finish();
                break;

            case R.id.main_btnSave:
                if(dataValidationCheck()){
                    saveImages();
                    mBuilding.setSynchronizedWithDatabase(false);
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

        if(!hasLocations){
            this.mViewPager.setCurrentItem(0,true);
            Toast.makeText(this,VALIDATE,Toast.LENGTH_SHORT).show();
            return false;
        }else if(!hasDetails){
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
        if(isUpdating){
            DBHelper.getInstance(this).deleteImagesByBuildingId(mBuilding.getId());
        }
        for (LocalImage localImage : images) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            localImage.getImage().compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            byte[] imageBytes = outputStream.toByteArray();
            localImage.setBuildingId(mBuilding.getId());
            DBHelper.getInstance(this).insertImage( localImage.getImagePath(),localImage.getTitle(),imageBytes, mBuilding.getId());
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
