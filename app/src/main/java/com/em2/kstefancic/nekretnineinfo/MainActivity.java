package com.em2.kstefancic.nekretnineinfo;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.em2.kstefancic.nekretnineinfo.LoginAndRegister.LoginActivity;
import com.em2.kstefancic.nekretnineinfo.api.model.RealEstate;
import com.em2.kstefancic.nekretnineinfo.api.model.User;
import com.em2.kstefancic.nekretnineinfo.api.service.RealEstateService;
import com.em2.kstefancic.nekretnineinfo.helper.DBHelper;
import com.em2.kstefancic.nekretnineinfo.helper.SessionManager;

import java.time.Instant;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static com.em2.kstefancic.nekretnineinfo.LoginAndRegister.LoginActivity.BASE_URL;

public class MainActivity extends AppCompatActivity{

    private User mUser;
    private SessionManager mSessionManager;
    private Retrofit mRetrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mUser = (User) getIntent().getExtras().getSerializable(LoginActivity.USER);
        getRealEstatesFromDatabase();

        this.mSessionManager = new SessionManager(this);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRealEstatesFromDatabase();
            }
        });

    }

    private void getRealEstatesFromDatabase() {
        setRetrofit();
        RealEstateService client = mRetrofit.create(RealEstateService.class);
        Call<List<RealEstate>> call = client.getRealEstates();

        call.enqueue(new Callback<List<RealEstate>>() {
          @Override
          public void onResponse(Call<List<RealEstate>> call, Response<List<RealEstate>> response) {
              Toast.makeText(getApplicationContext(),response.body().get(0).toString(),Toast.LENGTH_LONG).show();
          }

          @Override
          public void onFailure(Call<List<RealEstate>> call, Throwable t) {
              Toast.makeText(getApplicationContext(),t.toString(),Toast.LENGTH_LONG).show();
              Log.e("REAL_ESTATE",t.toString());
          }
        });
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
        this.mSessionManager.setLogin(false);
        DBHelper.getInstance(this).deleteUser();
        Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
    }


}
