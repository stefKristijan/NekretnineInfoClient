package com.kstefancic.nekretnineinfo;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.kstefancic.nekretnineinfo.api.model.Building;
import com.kstefancic.nekretnineinfo.api.model.BuildingLocation;
import com.kstefancic.nekretnineinfo.api.model.localDBdto.LocalImage;
import com.kstefancic.nekretnineinfo.helper.DBHelper;

import java.text.SimpleDateFormat;
import java.util.List;

import static com.kstefancic.nekretnineinfo.views.BuildingAdapter.BUILDING_INTENT;

public class BuildingDetailsActivity extends AppCompatActivity {

    private TextView tvDate, tvStreet, tvStreetNum, tvCity, tvState, tvPosition, tvCadastralParticle;
    private TextView tvYearOfBuild, tvPurpose, tvMaterial, tvCeiling, tvRoof, tvConstSyst;
    private TextView tvLength, tvWidth, tvResidentialArea, tvBusinessArea, tvBasementArea, tvBrutoArea;
    private TextView tvFloorHeight, tvFullHeight, tvNumOfFloors, tvProperGroundPlan, tvNumOfFlats;
    private ImageButton ibPrevious, ibNext, ibBack;
    private ImageView ivBuilding;
    private Building mBuilding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_details);

        mBuilding = (Building) getIntent().getSerializableExtra(BUILDING_INTENT);

        setUpUI();
    }

    private void setUpUI() {
        setUpActionBarButtons();
        setDateView();
        setUpPictures();
        setUpLocationInfo();
        setUpDetailsInfo();
        setUpDimensionsInfo();
    }

    private void setUpActionBarButtons() {
        this.ibBack = findViewById(R.id.buildingDetailsActvty_ibBack);

        this.ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void setDateView() {
        this.tvDate = findViewById(R.id.buildingDetailsActvty_tvDate);
        if(mBuilding.isSynchronizedWithDatabase()){
            this.tvDate.setBackground(getResources().getDrawable(R.drawable.cv_tv_synced));
        }else{
            this.tvDate.setBackground(getResources().getDrawable(R.drawable.cv_tv_not_sync));

        }
        String date = new SimpleDateFormat("dd. MMM yyyy. HH:mm").format(mBuilding.getDate());
        this.tvDate.setText(date);
    }

    private void setUpPictures() {
        this.ibNext = findViewById(R.id.buildingDetailsActvty_ibNext);
        this.ibPrevious = findViewById(R.id.buildingDetailsActvty_ibPrevious);
        this.ivBuilding=findViewById(R.id.buildingDetailsActvty_ivBuilding);

        final List<LocalImage> images = getImages();
        final LocalImage[] currentImage = {images.get(0)};
        this.ivBuilding.setImageBitmap(currentImage[0].getImage());
        this.ibNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = currentImage[0].getListId();
                if(index<images.size()-1){
                    ivBuilding.setImageBitmap(images.get(++index).getImage());
                    currentImage[0] =images.get(index);
                }


            }
        });

        this.ibPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = currentImage[0].getListId();
                if(index>0){
                    ivBuilding.setImageBitmap(images.get(--index).getImage());
                    currentImage[0] =images.get(index);
                }

            }
        });
    }

    @NonNull
    private List<LocalImage> getImages() {
        final List<LocalImage> images = DBHelper.getInstance(this).getImagesByBuildingId(mBuilding.getId());
        for(int i=0; i<images.size();i++){
            images.get(i).setListId(i);
        }
        return images;
    }

    private void setUpDimensionsInfo() {
        this.tvLength=findViewById(R.id.buildingDetailsActvty_tvLength);
        this.tvWidth=findViewById(R.id.buildingDetailsActvty_tvWidth);
        this.tvProperGroundPlan=findViewById(R.id.buildingDetailsActvty_tvProperGroundPlan);
        this.tvBrutoArea = findViewById(R.id.buildingDetailsActvty_tvBrutoArea);
        this.tvBusinessArea=findViewById(R.id.buildingDetailsActvty_tvBusinessArea);
        this.tvResidentialArea = findViewById(R.id.buildingDetailsActvty_tvResidentialArea);
        this.tvBasementArea=findViewById(R.id.buildingDetailsActvty_tvBasementArea);
        this.tvFullHeight = findViewById(R.id.buildingDetailsActvty_tvFullHeight);
        this.tvFloorHeight = findViewById(R.id.buildingDetailsActvty_tvFloorHeight);
        this.tvNumOfFloors = findViewById(R.id.buildingDetailsActvty_tvNumberOfFloors);
        this.tvNumOfFlats = findViewById(R.id.buildingDetailsActvty_tvNumberOfFlats);
        this.tvLength.setText(mBuilding.getLength()+" m");
        this.tvWidth.setText(mBuilding.getWidth()+" m");
        if(mBuilding.isProperGroundPlan()){
            this.tvProperGroundPlan.setText("DA");
        }else{
            this.tvProperGroundPlan.setText("NE");
        }
        this.tvBrutoArea.setText(Html.fromHtml(mBuilding.getBrutoArea()+" m<sup>2</sup>"));
        this.tvBusinessArea.setText(Html.fromHtml(mBuilding.getBusinessBrutoArea()+" m<sup>2</sup>"));
        this.tvBasementArea.setText(Html.fromHtml(mBuilding.getBasementBrutoArea()+" m<sup>2</sup>"));
        this.tvResidentialArea.setText(Html.fromHtml(mBuilding.getResidentialBrutoArea()+" m<sup>2</sup>"));
        this.tvFullHeight.setText(mBuilding.getFullHeight()+"m");
        this.tvFloorHeight.setText(mBuilding.getFloorHeight()+"m");
        this.tvNumOfFloors.setText(String.valueOf(mBuilding.getNumberOfFloors()));
        this.tvNumOfFlats.setText(String.valueOf(mBuilding.getNumberOfFlats()));
    }

    private void setUpDetailsInfo() {
        this.tvYearOfBuild = findViewById(R.id.buildingDetailsActvty_tvYearOfBuild);
        this.tvPurpose = findViewById(R.id.buildingDetailsActvty_tvPurpose);
        this.tvMaterial = findViewById(R.id.buildingDetailsActvty_tvMaterial);
        this.tvCeiling = findViewById(R.id.buildingDetailsActvty_tvCeilingMaterial);
        this.tvRoof = findViewById(R.id.buildingDetailsActvty_tvRoof);
        this.tvConstSyst = findViewById(R.id.buildingDetailsActvty_tvConstructionSystem);

        this.tvYearOfBuild.setText(mBuilding.getYearOfBuild());
        this.tvPurpose.setText(mBuilding.getPurpose().getPurpose());
        this.tvMaterial.setText(mBuilding.getMaterial().getMaterial());
        this.tvCeiling.setText(mBuilding.getCeilingMaterial().getCeilingMaterial());
        this.tvRoof.setText(mBuilding.getRoof().getRoofType());
        this.tvConstSyst.setText(mBuilding.getConstructionSystem().getConstructionSystem());
    }

    private void setUpLocationInfo() {
        this.tvStreetNum = findViewById(R.id.buildingDetailsActvty_tvStreetNumItems);
        this.tvCity = findViewById(R.id.buildingDetailsActvty_tvCityItem);
        this.tvState = findViewById(R.id.buildingDetailsActvty_tvState);
        this.tvPosition = findViewById(R.id.buildingDetailsActvty_tvPosition);
        this.tvStreet = findViewById(R.id.buildingDetailsActvty_tvStreetItems);
        this.tvCadastralParticle = findViewById(R.id.buildingDetailsActvty_tvCadastralPartItems);

        List<BuildingLocation> locations = mBuilding.getLocations();
        String streets ="";
        String streetNums = "";
        String cadastralParticles ="";
        for(int i=0; i<locations.size(); i++){
            BuildingLocation location = locations.get(i);
            if(i==0){
                streets = location.getStreet();
                streetNums = String.valueOf(location.getStreetNumber()+location.getStreetChar());
                cadastralParticles = location.getCadastralParticle();
            }else{
                boolean streetExists = false, numExists = false, cadastralExists =false;
                for(int j=0;j<i;j++){
                    BuildingLocation existingLoc = locations.get(j);
                    if (location.getStreet().equals(existingLoc.getStreet())) {
                        streetExists=true;
                    }
                    if(String.valueOf(location.getStreetNumber()+location.getStreetChar()).equals(String.valueOf(existingLoc.getStreetNumber()+existingLoc.getStreetChar()))){
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
                    streetNums += ", "+location.getStreetNumber()+location.getStreetChar();
                }
                if(!cadastralExists){
                    cadastralParticles+=", "+location.getCadastralParticle();
                }
            }
        }
        this.tvStreet.setText(streets);
        this.tvCity.setText(locations.get(0).getCity());
        this.tvState.setText(locations.get(0).getState());
        this.tvPosition.setText(mBuilding.getPosition().getPosition());
        this.tvStreetNum.setText(streetNums);
        this.tvCadastralParticle.setText(cadastralParticles);

    }
}
