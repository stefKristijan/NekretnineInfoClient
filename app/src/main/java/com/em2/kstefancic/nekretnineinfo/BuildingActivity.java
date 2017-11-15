package com.em2.kstefancic.nekretnineinfo;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.em2.kstefancic.nekretnineinfo.api.model.Building;
import com.em2.kstefancic.nekretnineinfo.api.service.BuildingService;
import com.em2.kstefancic.nekretnineinfo.views.BuildingAdapter;

import java.io.File;
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

public class BuildingActivity extends AppCompatActivity {

    private Retrofit mRetrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building);


        if(ContextCompat.checkSelfPermission(BuildingActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(BuildingActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
        }
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



       /* Call<ResponseBody> call = buildingService.uploadBuilding(setAuthenticationHeader(),mUser.getUsername(),parts,building);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("GOOD",response.toString());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Error", t.toString());
            }
        });*/
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
}
