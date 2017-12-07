package com.kstefancic.nekretnineinfo.helper;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.kstefancic.nekretnineinfo.api.exceptionutils.ErrorUtils;
import com.kstefancic.nekretnineinfo.api.model.Building;
import com.kstefancic.nekretnineinfo.api.model.ExceptionResponse;
import com.kstefancic.nekretnineinfo.api.model.ImagePath;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceDataResponse;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.CeilingMaterial;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.ConstructionSystem;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.Material;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.Position;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.Purpose;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.Roof;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.Sector;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.addressMultichoiceData.City;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.addressMultichoiceData.State;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.addressMultichoiceData.Street;
import com.kstefancic.nekretnineinfo.api.model.MultichoiceLocationDataResponse;
import com.kstefancic.nekretnineinfo.api.model.User;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kstefancic.nekretnineinfo.LoginAndRegister.LoginActivity.USER;
import static com.kstefancic.nekretnineinfo.helper.RetrofitSingleton.BASE_URL;

/**
 * Created by user on 6.12.2017..
 */

public class GetDataService extends Service {

    private static final String DEFAULT_ERROR = "Došlo je do pogreške. Pokušajte ponovno kasnije.";
    public static final String FINISH = "finish";
    private User mUser;
    private SessionManager mSessionManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mUser = (User) intent.getSerializableExtra(USER);
        mSessionManager = new SessionManager(this);
        getMultiChoiceDataAndSaveToLocalDatabase();
        Log.i("SERVICE", "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }



    private void getMultiChoiceDataAndSaveToLocalDatabase() {

        String base = mUser.getUsername()+":"+mSessionManager.getPassword();
        String authHeader = "Basic "+ Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);

        final Call<MultiChoiceDataResponse> getMultiChoiceCall = RetrofitSingleton.getMultiChoiceDataService().getMultiChoiceData(authHeader);
        getMultiChoiceCall.enqueue(new Callback<MultiChoiceDataResponse>() {
            @Override
            public void onResponse(Call<MultiChoiceDataResponse> call, Response<MultiChoiceDataResponse> response) {
                if (response.isSuccessful()){
                    Log.i("SERVICE","multichoice data success");
                    insertPositionsInLocalDatabase(response.body().getPosition());
                    insertMaterialsInLocalDatabase(response.body().getMaterial());
                    insertConstructionSystemsInLocalDatabase(response.body().getConstructionSystem());
                    insertPurposesInLocalDatabase(response.body().getPurpose());
                    insertCeilingMaterialsInLocalDatabase(response.body().getCeilingMaterial());
                    insertSectorsInLocalDatabase(response.body().getSectors());
                    insertRoofsInLocalDatabase(response.body().getRoofs());
                    getMultichoiceAddressDataAndSaveToLocalDatabase();
                }else{
                    Log.e("MULTICHOICE DATA RESP", response.body().toString());
                    showErrorResponse(response);
                }
            }

            @Override
            public void onFailure(Call<MultiChoiceDataResponse> call, Throwable t) {
                Log.e("MULTICHOICE DATA RESP", t.toString());
                showDefaultError();
            }
        });
    }



    private void getMultichoiceAddressDataAndSaveToLocalDatabase() {
        String base = mUser.getUsername()+":"+mSessionManager.getPassword();
        String authHeader = "Basic "+ Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);

        final Call<MultichoiceLocationDataResponse> getMultiChoiceLocationCall = RetrofitSingleton.getMultiChoiceDataService().getMultichoiceLocationData(authHeader);
        getMultiChoiceLocationCall.enqueue(new Callback<MultichoiceLocationDataResponse>() {
            @Override
            public void onResponse(Call<MultichoiceLocationDataResponse> call, Response<MultichoiceLocationDataResponse> response) {
                if (response.isSuccessful()){
                    Log.i("SERVICE","multichoice location data success");
                    insertStatesInLocalDatabase(response.body().getStates());
                    insertCitiesInLocalDatabase(response.body().getCities());
                    insertStreetsInLocalDatabase(response.body().getStreets());
                    getBuildingsFromServer();
                }else{
                    Log.e("LOCATION DATA RESP", response.body().toString());
                    showErrorResponse(response);
                }
            }

            @Override
            public void onFailure(Call<MultichoiceLocationDataResponse> call, Throwable t) {
                Log.e("MULTICHOICE DATA RESP", t.toString());
                showDefaultError();
            }
        });
    }

    private void getBuildingsFromServer() {
        Log.i("LOGIN", "getting buildings from serer");
        Call<List<Building>> call = RetrofitSingleton.getBuildingService().getBuildings(setAuthenticationHeader(),mUser.getUsername());

        call.enqueue(new Callback<List<Building>>() {
            @Override
            public void onResponse(Call<List<Building>> call, Response<List<Building>> response) {
                if(response.isSuccessful()){
                    Log.d("ON BUILDING RESPONSE", response.body().toString());
                    for(Building building : response.body()){
                        DBHelper.getInstance(getApplicationContext()).insertBuilding(building);
                        insertPicturesIntoDatabase(building);
                    }

                }else {
                    Log.e("ON BUILDING RESPONSE", response.raw().toString());
                }

            }

            @Override
            public void onFailure(Call<List<Building>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.toString(),Toast.LENGTH_LONG).show();
                Log.e("REAL_ESTATE",t.toString());
            }
        });
    }

    private void insertPicturesIntoDatabase(final Building building) {
        for(int i=0; i<building.getImagePaths().size();i++){
            final ImagePath imagePath = building.getImagePaths().get(i);
            final int finalI = i;
            Picasso.with(this)
                    .load(BASE_URL+"/"+imagePath.getPath())
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                            String path = saveThumbnailToInternalStorage(bitmap,imagePath.getTitle());
                            Log.d("INSERTING IMAGE",imagePath.getPath()+"   "+path);
                            DBHelper.getInstance(getApplicationContext()).insertImage(path,imagePath.getTitle(),outputStream.toByteArray(),building.getId());
                            if(finalI == building.getImagePaths().size()-1){
                                notifyFinish();
                            }
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {
                            Log.d("FAIL INSERTING IMAGE",imagePath.getPath());
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                            Log.d("PREPARE INSERTING IMAGE",imagePath.getPath());
                        }
                    });
        }
    }

    private String saveThumbnailToInternalStorage(Bitmap imageBitmap, String fileName) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,fileName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mypath.getAbsolutePath();
    }

    private String setAuthenticationHeader() {
        String base = mUser.getUsername()+":"+this.mSessionManager.getPassword();
        return "Basic "+ Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);
    }

    private void notifyFinish() {
        Intent intent = new Intent(FINISH);
        intent.putExtra(FINISH, true);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void insertRoofsInLocalDatabase(List<Roof> roofs) {
        for(Roof roof : roofs){
            DBHelper.getInstance(this).insertRoof(roof);
        }
    }


    private void insertStatesInLocalDatabase(List<State> states) {
        for (State state : states){
            DBHelper.getInstance(this).insertState(state);
        }
    }

    private void insertCitiesInLocalDatabase(List<City> cities) {
        for (City city : cities){
            DBHelper.getInstance(this).insertCity(city);
        }
    }

    private void insertStreetsInLocalDatabase(List<Street> streets) {
        for (Street street : streets){
            DBHelper.getInstance(this).insertStreet(street);
        }
    }

    private void insertCeilingMaterialsInLocalDatabase(List<CeilingMaterial> ceilingMaterials) {

        for(CeilingMaterial ceilingMaterial : ceilingMaterials) {
            DBHelper.getInstance(this).insertCeilingMaterial(ceilingMaterial);
        }
    }

    private void insertPurposesInLocalDatabase(List<Purpose> purposes) {
        for(Purpose purpose : purposes){
            DBHelper.getInstance(this).insertPurpose(purpose);
        }
    }

    private void insertConstructionSystemsInLocalDatabase(List<ConstructionSystem> constructionSystems) {
        for(ConstructionSystem constructionSystem : constructionSystems){
            DBHelper.getInstance(this).insertConstructSys(constructionSystem);
        }
    }

    private void insertMaterialsInLocalDatabase(List<Material> materials) {
        for(Material material : materials){
            DBHelper.getInstance(this).insertMaterial(material);
        }
    }

    private void insertPositionsInLocalDatabase(List<Position> positions) {
        for(Position position : positions){
            DBHelper.getInstance(this).insertPosition(position);
        }
    }

    private void insertSectorsInLocalDatabase(List<Sector> sectors) {
        for(Sector sector : sectors){
            DBHelper.getInstance(this).insertSector(sector);
        }

    }

    private void showErrorResponse(Response<?> response) {
        ExceptionResponse exceptionResponse = ErrorUtils.parseError(response,RetrofitSingleton.getRetrofitInstance());
        Toast.makeText(getApplicationContext(),exceptionResponse.getMessage(),Toast.LENGTH_SHORT).show();
    }

    private void showDefaultError() {
        Toast.makeText(getApplicationContext(),DEFAULT_ERROR,Toast.LENGTH_SHORT).show();
    }
}
