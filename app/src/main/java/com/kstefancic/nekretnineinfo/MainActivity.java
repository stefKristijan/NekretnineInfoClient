package com.kstefancic.nekretnineinfo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.kstefancic.nekretnineinfo.LoginAndRegister.LoginActivity;
import com.kstefancic.nekretnineinfo.api.model.Building;
import com.kstefancic.nekretnineinfo.api.model.ImagePath;
import com.kstefancic.nekretnineinfo.api.model.User;
import com.kstefancic.nekretnineinfo.helper.DBHelper;
import com.kstefancic.nekretnineinfo.helper.RetrofitSingleton;
import com.kstefancic.nekretnineinfo.helper.SessionManager;
import com.kstefancic.nekretnineinfo.views.BuildingAdapter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kstefancic.nekretnineinfo.LoginAndRegister.LoginActivity.FIRST_LOGIN;
import static com.kstefancic.nekretnineinfo.LoginAndRegister.LoginActivity.USER;
import static com.kstefancic.nekretnineinfo.helper.RetrofitSingleton.BASE_URL;
import static com.kstefancic.nekretnineinfo.views.BuildingAdapter.UPDATE_BUILDING_RQST;

public class MainActivity extends AppCompatActivity{

    private static final int NEW_BUILDING_RQST = 1;
    public static final String BUILDING_DATA = "building";
    private static final int LOGIN_RQST = 10;
    private User mUser;
    private SessionManager mSessionManager;
    private RecyclerView recyclerView;
    private BuildingAdapter buildingAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.ItemDecoration itemDecoration;
    private List<Building> buildings = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkIfLoggedIn();
        setUpActivity();
        //DBHelper.getInstance(this).deleteBuildings();
        getBuildingsFromLocalDatabase();
    }

    private void checkIfLoggedIn() {
        this.mSessionManager = new SessionManager(this);
        //this.mSessionManager.setLogin(false,null);
        //DBHelper.getInstance(this).deleteAllTables();
        if(!this.mSessionManager.isLoggedIn()){
            Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
            startActivityForResult(loginIntent, LOGIN_RQST);
        }else{
            this.mUser=DBHelper.getInstance(this).getUser();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
        if(requestCode==LOGIN_RQST){
            this.mUser = (User) data.getSerializableExtra(USER);
            getBuildingsFromServer();
        }
        if(requestCode==UPDATE_BUILDING_RQST){
            Building building = (Building) data.getSerializableExtra(BUILDING_DATA);
            Log.d("ONRESULT", building.toString());
            building.setUser(mUser);
            building.setDate(new Timestamp(System.currentTimeMillis()));
            for(int i = 0; i<buildings.size();i++){
                if(Objects.equals(buildings.get(i).getId(), building.getId())){
                    buildings.remove(i);
                    break;
                }
            }
            buildings.add(building);
            DBHelper.getInstance(this).updateBuilding(building);
            Log.d("ONRESULT AFTER INSERT", building.toString());
            buildingAdapter.notifyDataSetChanged();
        }
    }

    private void setUpActivity() {
        this.mSessionManager = new SessionManager(this);

        this.recyclerView = findViewById(R.id.rvBuilding);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent buildingIntent = new Intent(MainActivity.this,BuildingDataActivity.class);
                startActivityForResult(buildingIntent, NEW_BUILDING_RQST);
            }
        });
    }

    private void getBuildingsFromLocalDatabase() {
        buildings = DBHelper.getInstance(this).getAllBuildings();
        setRecyclerView(buildings);
    }

    private void getBuildingsFromServer() {
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
                    .load(BASE_URL+imagePath.getImagePath())
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                            byte[] imageBytes = outputStream.toByteArray();
                            Log.d("INSERTING IMAGE",imagePath.getImagePath());
                            DBHelper.getInstance(getApplicationContext()).insertImage(imageBytes,building.getId());
                            getBuildingsFromLocalDatabase();
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {
                            Log.d("FAIL INSERTING IMAGE",imagePath.getImagePath());
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                            Log.d("PREPARE INSERTING IMAGE",imagePath.getImagePath());
                        }
                    });
        }
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logOut) {
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

        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        this.mSessionManager.setLogin(false,"");
        DBHelper.getInstance(this).deleteAllTables();
        Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
    }


}
