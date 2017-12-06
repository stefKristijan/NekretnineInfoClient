package com.kstefancic.nekretnineinfo;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.kstefancic.nekretnineinfo.LoginAndRegister.LoginActivity;
import com.kstefancic.nekretnineinfo.api.model.Building;
import com.kstefancic.nekretnineinfo.api.model.ImagePath;
import com.kstefancic.nekretnineinfo.api.model.User;
import com.kstefancic.nekretnineinfo.api.model.localDBdto.LocalImage;
import com.kstefancic.nekretnineinfo.helper.DBHelper;
import com.kstefancic.nekretnineinfo.helper.RetrofitSingleton;
import com.kstefancic.nekretnineinfo.helper.SessionManager;
import com.kstefancic.nekretnineinfo.views.BuildingAdapter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kstefancic.nekretnineinfo.LoginAndRegister.LoginActivity.FIRST_LOGIN;
import static com.kstefancic.nekretnineinfo.LoginAndRegister.LoginActivity.USER;
import static com.kstefancic.nekretnineinfo.helper.RetrofitSingleton.BASE_URL;
import static com.kstefancic.nekretnineinfo.views.BuildingAdapter.UPDATE_BUILDING_RQST;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int NEW_BUILDING_RQST = 1;
    public static final String BUILDING_DATA = "building";
    private static final int LOGIN_RQST = 2;
    private User mUser;
    private SessionManager mSessionManager;
    private RecyclerView recyclerView;
    private ImageButton ibSynchronize, ibLogout;
    private BuildingAdapter buildingAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.ItemDecoration itemDecoration;
    private List<Building> buildings = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpActivity();
        this.mUser = (User) getIntent().getExtras().getSerializable(USER);

        if(getIntent().getExtras().getBoolean(FIRST_LOGIN)){
            Log.d("GET BUILDINGS", "getting buildings from server");

            getBuildingsFromServer();
        }else{
            Log.d("GET BUILDINGS", "getting buildings from local database");
            getBuildingsFromLocalDatabase();
        }
        //DBHelper.getInstance(this).deleteBuildings();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("RESULT",String.valueOf(requestCode)+" "+resultCode);
        if (requestCode == NEW_BUILDING_RQST) {
            Log.d("REQUEST CODE", String.valueOf(NEW_BUILDING_RQST));
            if (resultCode == RESULT_OK) {
                Building building = (Building) data.getSerializableExtra(BUILDING_DATA);
                Log.d("ONRESULT", building.toString());
                building.setUser(mUser);
                building.setDate(new Timestamp(System.currentTimeMillis()));
                buildings.add(building);
                DBHelper.getInstance(this).insertBuilding(building);
                Log.d("ONRESULT AFTER INSERT", building.toString());
                setRecyclerView(buildings);
            } else if (resultCode == RESULT_CANCELED) {
                Log.i("RESULT_CANCEL", "result canceled");
            }
        }

        if(requestCode==UPDATE_BUILDING_RQST){
            if(resultCode==RESULT_OK) {
                Building building = (Building) data.getSerializableExtra(BUILDING_DATA);
                Log.d("ONRESULT", building.toString());
                building.setUser(mUser);
                building.setDate(new Timestamp(System.currentTimeMillis()));
                for (int i = 0; i < buildings.size(); i++) {
                    if (Objects.equals(buildings.get(i).getId(), building.getId())) {
                        buildings.remove(i);
                        break;
                    }
                }
                buildings.add(building);
                DBHelper.getInstance(this).updateBuildingById(building);
                Log.d("ONRESULT AFTER INSERT", building.toString());
                buildingAdapter.notifyDataSetChanged();
            }
        }
    }

    private void setUpActivity() {
        this.ibLogout = findViewById(R.id.main_ibLogout);
        this.ibLogout.setOnClickListener(this);
        this.ibSynchronize=findViewById(R.id.main_ibSynchronize);
        this.ibSynchronize.setOnClickListener(this);

        this.mSessionManager = new SessionManager(this);

        this.recyclerView = findViewById(R.id.rvBuilding);

        FloatingActionButton fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(this);
    }

    private void getBuildingsFromLocalDatabase() {
        buildings = DBHelper.getInstance(this).getAllBuildings();
        setRecyclerView(buildings);
    }

    private void getBuildingsFromServer() {
        Log.i("LOGIN", "getting buildings from serer");
        Call<List<Building>> call = RetrofitSingleton.getBuildingService().getBuildings(setAuthenticationHeader(),mUser.getUsername());

        call.enqueue(new Callback<List<Building>>() {
          @Override
          public void onResponse(Call<List<Building>> call, Response<List<Building>> response) {
              if(response.isSuccessful()){
                  Log.d("ON BUILDING RESPONSE", response.body().toString());
                  buildings = response.body();
                  for(Building building : buildings){
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
        for(final ImagePath imagePath : building.getImagePaths()){
            Picasso.with(this)
                    .load(BASE_URL+"/"+imagePath.getPath())
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                            getBuildingsFromLocalDatabase();
                            //Uri finalUri = getImageUri(getApplicationContext(),bitmap,imagePath);
                            String path = saveThumbnailToInternalStorage(bitmap,imagePath.getTitle());
                            Log.d("INSERTING IMAGE",imagePath.getPath()+"   "+path);
                            DBHelper.getInstance(getApplicationContext()).insertImage(path,imagePath.getTitle(),outputStream.toByteArray(),building.getId());
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

    public String getRealPathFromUri(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
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

    private Uri getImageUri(Context context, Bitmap imageBitmap, ImagePath imagePath) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd").format(new Date());
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG,100,bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), imageBitmap, imagePath.getTitle(),null);
        return Uri.parse(path);
    }

    private void setRecyclerView(List<Building> buildings) {

        this.buildingAdapter= new BuildingAdapter(buildings,this, mSessionManager);
        this.layoutManager = new LinearLayoutManager(this);
        this.itemDecoration= new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);

        this.recyclerView.addItemDecoration(this.itemDecoration);
        this.recyclerView.setLayoutManager(this.layoutManager);
        this.recyclerView.setAdapter(this.buildingAdapter);
    }

    private String setAuthenticationHeader() {
        String base = mUser.getUsername()+":"+this.mSessionManager.getPassword();
        return "Basic "+ Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);
    }


    private void signOut() {
        this.mSessionManager.setLogin(false,"");
        DBHelper.getInstance(this).deleteAllTables();
        Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.fabAdd:
                Intent buildingIntent = new Intent(MainActivity.this,BuildingDataActivity.class);
                startActivityForResult(buildingIntent, NEW_BUILDING_RQST);
                break;

            case R.id.main_ibSynchronize:
                synchronizeBuildings();
                break;

            case R.id.main_ibLogout:
               showLogoutDialog();
                break;
        }
    }

    private void synchronizeBuildings() {
        for(int i=0; i<buildings.size(); i++){
            if(!buildings.get(i).isSynchronizedWithDatabase()){
                uploadBuilding(DBHelper.getInstance(this).getImagesByBuildingId(buildings.get(i).getId()),i);
            }
        }
    }

    private void uploadBuilding(List<LocalImage> images, final int position) {

        List<MultipartBody.Part> parts = new ArrayList<>();
        Log.d("fileURIs size",String.valueOf(images.size()));
        for(int i=0; i< images.size();i++){
            parts.add(prepareFilePart("files",images.get(i).getImagePath()));
        }
        Log.d("BEFORE UPLOAD",setAuthenticationHeader()+"\n"+buildings.get(position).getUser().toString()+"\n"+parts.size()+"\n"+buildings.get(position).toString());
        Call<Building> call = RetrofitSingleton.getBuildingService().uploadBuilding(setAuthenticationHeader(),buildings.get(position).getUser().getUsername(),parts,buildings.get(position));

        call.enqueue(new Callback<Building>() {
            @Override
            public void onResponse(Call<Building> call, Response<Building> response) {
                Log.d("GOOD",response.toString());
                if(response.isSuccessful()){
                    Building building = response.body();
                    Log.i("BUILDING",building.toString());
                    DBHelper.getInstance(getApplicationContext()).updateLocations(buildings.get(position).getId(), building.getId());
                    DBHelper.getInstance(getApplicationContext()).updateImagesByBuildingId(buildings.get(position).getId(), building.getId());
                    buildings.get(position).setId(building.getId());
                    buildings.get(position).setSynchronizedWithDatabase(true);
                    buildings.get(position).setLocations(building.getLocations());
                    DBHelper.getInstance(getApplicationContext()).updateBuildingByuId(building);
                    buildingAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<Building> call, Throwable t) {
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

    private void showLogoutDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Odjava")
                .setMessage("Jeste li sigurni da se želite odjaviti?")
                .setNegativeButton("Odustani", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Odjavi se", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        signOut();
                    }
                })
                .show();
    }
}
