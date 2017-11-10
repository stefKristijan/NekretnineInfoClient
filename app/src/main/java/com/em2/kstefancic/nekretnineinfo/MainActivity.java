package com.em2.kstefancic.nekretnineinfo;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.em2.kstefancic.nekretnineinfo.LoginAndRegister.LoginActivity;
import com.em2.kstefancic.nekretnineinfo.api.model.Building;
import com.em2.kstefancic.nekretnineinfo.api.model.User;
import com.em2.kstefancic.nekretnineinfo.api.service.BuildingService;
import com.em2.kstefancic.nekretnineinfo.helper.DBHelper;
import com.em2.kstefancic.nekretnineinfo.helper.SessionManager;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
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
    private Building building;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mUser = (User) getIntent().getExtras().getSerializable(LoginActivity.USER);
        getRealEstatesFromDatabase();

        this.mSessionManager = new SessionManager(this);

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
        }

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

    private void getRealEstatesFromDatabase() {
        setRetrofit();
        BuildingService client = mRetrofit.create(BuildingService.class);
        Call<List<Building>> call = client.getBuildings();

        call.enqueue(new Callback<List<Building>>() {
          @Override
          public void onResponse(Call<List<Building>> call, Response<List<Building>> response) {
              //Toast.makeText(getApplicationContext(),response.body().get(0).toString(),Toast.LENGTH_LONG).show();
              building = response.body().get(0);

              uploadToServer();
          }

          @Override
          public void onFailure(Call<List<Building>> call, Throwable t) {
              Toast.makeText(getApplicationContext(),t.toString(),Toast.LENGTH_LONG).show();
              Log.e("REAL_ESTATE",t.toString());
          }
        });
    }

    private void uploadToServer() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String picturesDirectoryPath = pictureDirectory.getPath();

        Uri data = Uri.parse(picturesDirectoryPath);

        intent.setDataAndType(data,"image/*");

        startActivityForResult(intent, 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK && data != null) {
            ClipData clipData = data.getClipData();
            ArrayList<Uri> fileUris = new ArrayList<>();

            for (int i = 0; i < clipData.getItemCount(); i++) {
                ClipData.Item item = clipData.getItemAt(i);
                Uri uri = item.getUri();
                Log.d("URI", uri.toString());
                fileUris.add(uri);
            }
            uploadAlbum(fileUris);
        }
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null,
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

    private void uploadAlbum(ArrayList<Uri> fileUris) {
        BuildingService buildingService = mRetrofit.create(BuildingService.class);

        List<MultipartBody.Part> parts = new ArrayList<>();
        Log.d("fileURIs size",String.valueOf(fileUris.size()));
        for(int i=0; i< fileUris.size();i++){
            parts.add(prepareFilePart("files",fileUris.get(i)));
        }



        Call<ResponseBody> call = buildingService.uploadBuilding(parts,building);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("GOOD",response.toString());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Error", t.toString());
            }
        });
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri){
        String imagePath = getRealPathFromURI(this, fileUri);
        Log.d("IMAGE PATH",imagePath);
        File imageFile = new File(imagePath);

        RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(fileUri)),imageFile);

        MultipartBody.Part part = MultipartBody.Part.createFormData(partName, imageFile.getName(), requestFile);
        Log.d("MULTIPARTBODY",part.body().contentType().toString());
        return part;
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
