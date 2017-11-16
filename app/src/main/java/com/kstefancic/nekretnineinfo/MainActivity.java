package com.kstefancic.nekretnineinfo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
import com.kstefancic.nekretnineinfo.api.service.BuildingService;
import com.kstefancic.nekretnineinfo.helper.DBHelper;
import com.kstefancic.nekretnineinfo.helper.SessionManager;
import com.kstefancic.nekretnineinfo.views.BuildingAdapter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static com.kstefancic.nekretnineinfo.LoginAndRegister.LoginActivity.BASE_URL;
import static com.kstefancic.nekretnineinfo.LoginAndRegister.LoginActivity.FIRST_LOGIN;
import static com.kstefancic.nekretnineinfo.LoginAndRegister.LoginActivity.USER;

public class MainActivity extends AppCompatActivity{

    private static final int NEW_BUILDING_RQST = 1;
    public static final String BUILDING_DATA = "building";
    private User mUser;
    private SessionManager mSessionManager;
    private Retrofit mRetrofit;
    private RecyclerView recyclerView;
    private BuildingAdapter buildingAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.ItemDecoration itemDecoration;
    private List<Building> buildings;


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


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_BUILDING_RQST) {
            Log.d("REQUEST CODE", String.valueOf(NEW_BUILDING_RQST));
            if (resultCode == RESULT_OK) {
                Building building = (Building) data.getSerializableExtra(BUILDING_DATA);
                buildings.add(building);
                Log.d("ONRESULT", building.toString());
                buildingAdapter.notifyDataSetChanged();
            } else if (resultCode == RESULT_CANCELED) {
                Log.i("RESULT_CANCEL", "result canceled");
            }
        }
    }

    private void setUpActivity() {
        setRetrofit();
        this.mSessionManager = new SessionManager(this);

        this.recyclerView = findViewById(R.id.rvBuilding);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent buildingIntent = new Intent(MainActivity.this,BuildingActivity.class);
                buildingIntent.putExtra(USER, mUser);
                startActivityForResult(buildingIntent, NEW_BUILDING_RQST);
            }
        });
    }

    private void getBuildingsFromLocalDatabase() {
        buildings = DBHelper.getInstance(this).getAllBuildings();
        setRecyclerView(buildings);
    }

    private void getBuildingsFromServer() {
        BuildingService client = mRetrofit.create(BuildingService.class);
        Call<List<Building>> call = client.getBuildings(setAuthenticationHeader(),mUser.getUsername());

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
                            DBHelper.getInstance(getApplicationContext()).insertImage(imagePath.getImagePath(),imageBytes,building.getId());
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

        Context context = getApplicationContext();
        this.buildingAdapter= new BuildingAdapter(buildings,context, mSessionManager);
        this.layoutManager = new LinearLayoutManager(context);
        this.itemDecoration= new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);

        this.recyclerView.addItemDecoration(this.itemDecoration);
        this.recyclerView.setLayoutManager(this.layoutManager);
        this.recyclerView.setAdapter(this.buildingAdapter);
    }

    private String setAuthenticationHeader() {
        String base = mUser.getUsername()+":"+this.mSessionManager.getPassword();
        return "Basic "+ Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);
    }

    private void setRetrofit() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create());

        mRetrofit = builder.build();

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
