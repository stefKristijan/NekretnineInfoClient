package com.kstefancic.potresnirizik.helper;

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

import com.kstefancic.potresnirizik.api.exceptionutils.ErrorUtils;
import com.kstefancic.potresnirizik.api.model.Building;
import com.kstefancic.potresnirizik.api.model.ExceptionResponse;
import com.kstefancic.potresnirizik.api.model.ImagePath;
import com.kstefancic.potresnirizik.api.model.MultiChoiceDataResponse;
import com.kstefancic.potresnirizik.api.model.MultiChoiceModels.CeilingMaterial;
import com.kstefancic.potresnirizik.api.model.MultiChoiceModels.Construction;
import com.kstefancic.potresnirizik.api.model.MultiChoiceModels.Material;
import com.kstefancic.potresnirizik.api.model.MultiChoiceModels.Position;
import com.kstefancic.potresnirizik.api.model.MultiChoiceModels.Purpose;
import com.kstefancic.potresnirizik.api.model.MultiChoiceModels.Roof;
import com.kstefancic.potresnirizik.api.model.MultiChoiceModels.Sector;
import com.kstefancic.potresnirizik.api.model.MultiChoiceModels.SupportingSystem;
import com.kstefancic.potresnirizik.api.model.MultiChoiceModels.addressMultichoiceData.City;
import com.kstefancic.potresnirizik.api.model.MultiChoiceModels.addressMultichoiceData.State;
import com.kstefancic.potresnirizik.api.model.MultiChoiceModels.addressMultichoiceData.Street;
import com.kstefancic.potresnirizik.api.model.MultichoiceLocationDataResponse;
import com.kstefancic.potresnirizik.api.model.User;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kstefancic.potresnirizik.LoginAndRegister.LoginActivity.USER;
import static com.kstefancic.potresnirizik.helper.RetrofitSingleton.BASE_URL;

/**
 * Created by user on 6.12.2017..
 */

public class GetDataService extends Service {

    private static final String DEFAULT_ERROR = "Došlo je do pogreške. Pokušajte ponovno kasnije.";
    public static final String FINISH = "finish";
    private User mUser;
    private SessionManager mSessionManager;
    private List<Target> targets = new ArrayList<>();

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
                    insertPositionsInLocalDatabase(response.body().getPositions());
                    //insertMaterialsInLocalDatabase(response.body().getMaterial());
                    insertConstructionsInLocalDatabase(response.body().getConstructions());
                    insertPurposesInLocalDatabase(response.body().getPurposes());
                    insertCeilingMaterialsInLocalDatabase(response.body().getCeilingMaterials());
                    insertSectorsInLocalDatabase(response.body().getSectors());
                    insertRoofsInLocalDatabase(response.body().getRoofs());
                    insertSupportingSystemsInLocalDatabase(response.body().getSupportingSystems());
                    //getMultichoiceAddressDataAndSaveToLocalDatabase();
                    getBuildingsFromServer();
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
                    if(response.body().size()==0){
                        notifyFinish();
                        stopSelf();
                    }
                    List<ImagePath> allImages = new ArrayList<>();
                    for(Building building: response.body()){
                        DBHelper.getInstance(getApplicationContext()).insertBuilding(building);
                        for(ImagePath imagePath : building.getImagePaths()){
                            allImages.add(imagePath);
                        }
                    }
                    insertPicturesIntoDatabase(allImages);
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

    private void insertPicturesIntoDatabase(final List<ImagePath> imagePaths) {
        for(int i=0; i<imagePaths.size();i++){
            final ImagePath imagePath = imagePaths.get(i);
            final int finalI = i;
            final Target target = new Target() {

                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    Log.d("IMAGE LOADED", imagePath.toString() + " "+finalI);
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                    String path = saveThumbnailToInternalStorage(bitmap,imagePath.getTitle());
                    DBHelper.getInstance(getApplicationContext()).insertImage(path,imagePath.getTitle(),outputStream.toByteArray(),imagePath.getBuildingId());
                    if(finalI == imagePaths.size()-1){
                        Log.i("SERVICE","DONE");
                        targets.clear();
                        notifyFinish();
                        stopSelf();
                    }
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    Log.d("FAIL INSERTING IMAGE",imagePath.getPath());
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                    Log.d("PREPARE INSERTING IMAGE",imagePath.getPath());
                    /*if(finalI == building.getImagePaths().size()-1){
                        notifyFinish();
                    }*/
                }
            };
            targets.add(target);

            Picasso.with(this)
                    .load(BASE_URL+"/"+imagePath.getPath())
                    .into(targets.get(i));
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

    private void insertConstructionsInLocalDatabase(List<Construction> constructions) {
        for(Construction construction : constructions){
            DBHelper.getInstance(this).insertConstruction(construction);
        }
    }

    private void insertSupportingSystemsInLocalDatabase(List<SupportingSystem> supportingSystems) {
        for(SupportingSystem supportingSystem: supportingSystems){
            DBHelper.getInstance(this).insertSupportingSystem(supportingSystem);
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
