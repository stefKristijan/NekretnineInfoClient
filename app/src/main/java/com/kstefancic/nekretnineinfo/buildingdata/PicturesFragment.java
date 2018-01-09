package com.kstefancic.nekretnineinfo.buildingdata;


import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.kstefancic.nekretnineinfo.LoginAndRegister.LoginActivity;
import com.kstefancic.nekretnineinfo.R;
import com.kstefancic.nekretnineinfo.api.model.Building;
import com.kstefancic.nekretnineinfo.api.model.localDBdto.LocalImage;
import com.kstefancic.nekretnineinfo.helper.DBHelper;
import com.kstefancic.nekretnineinfo.views.PictureGridViewAdapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import static android.app.Activity.RESULT_OK;
import static com.kstefancic.nekretnineinfo.MainActivity.BUILDING_DATA;

/**
 * Created by user on 15.11.2017..
 */

public class PicturesFragment extends Fragment{

    private static final int REQUEST_IMAGE_CAPTURE = 20;
    private static final int GALLERY_REQUEST = 10;
    private GridView gvPictures;
    private PictureGridViewAdapter pictureGridViewAdapter;
    private PictureChoosen pictureChoosenListener;
    private FloatingActionButton fabCamera;
    private ArrayList<LocalImage> localImages = new ArrayList<>();
    private Building mBuilding;

    public static PicturesFragment newInstance(Building building) {
        PicturesFragment fragment = new PicturesFragment();
        Bundle bundle = new Bundle();

        bundle.putSerializable(BUILDING_DATA, building);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_pictures,null);
        mBuilding = (Building) getArguments().getSerializable(BUILDING_DATA);
        if(mBuilding!=null){
            localImages = (ArrayList<LocalImage>) DBHelper.getInstance(getActivity()).getImagesByBuildingId(mBuilding.getId());
            Log.i("PICTURES", String.valueOf(localImages.size()));
        }
        setUI(layout);
        return layout;
    }

    private void setUI(View layout) {
        this.gvPictures = layout.findViewById(R.id.frPictures_gridView);
        pictureGridViewAdapter = new PictureGridViewAdapter(getActivity(), R.layout.picture_grid_item, localImages, pictureChoosenListener);
        gvPictures.setAdapter(pictureGridViewAdapter);

        this.fabCamera = layout.findViewById(R.id.frPictures_fabCamera);
        this.fabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] items = {"Otvori galeriju", "Koristi kameru", "Odustani"};

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Odaberite radnju");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch(item){
                            case 0:
                                openGallery();
                                break;

                            case 1:
                                startCamera();
                                break;

                        }
                        // Do something with the selection
                        dialog.dismiss();
                    }
                });
                builder.show();

            }
        });


    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String picturesDirectoryPath = pictureDirectory.getPath();

        Uri data = Uri.parse(picturesDirectoryPath);

        intent.setDataAndType(data, "image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, GALLERY_REQUEST);
    }


    private void startCamera(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            String path=saveThumbnailToInternalStorage(imageBitmap);
            Log.i("CAMERA IMAGE",path);
            addImageToList(imageBitmap, path);
            pictureGridViewAdapter.notifyDataSetChanged();
            pictureChoosenListener.onPictureChoosenListener(localImages);
        }
        else if (requestCode == 10 && resultCode == RESULT_OK && data != null) {
            ClipData clipData = data.getClipData();

            for (int i = 0; i < clipData.getItemCount(); i++) {
                ClipData.Item item = clipData.getItemAt(i);
                Uri uri = item.getUri();
                String path= getRealPathFromUri(uri);
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                addImageToList(bitmap, path);
                Log.d("INSERTING IMAGE", uri.toString() + " size: " + localImages.size()+ " realPath:"+path);
            }
            pictureGridViewAdapter.notifyDataSetChanged();
            pictureChoosenListener.onPictureChoosenListener(localImages);

        }
    }

    private String saveThumbnailToInternalStorage(Bitmap imageBitmap) {
        ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,getFileName()+".jpg");

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

    private String getFileName() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return String.valueOf("building_" + timeStamp);
    }

    private Uri getImageUri(Context context, Bitmap imageBitmap) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String imageFileName = String.valueOf("building_" + timeStamp);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG,100,bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), imageBitmap, imageFileName,"This is description");
        Log.i("NEWIMAGE", imageFileName+ "  "+path);
        return Uri.parse(path);
    }

    private void addImageToList(Bitmap bitmap, String path) {
        LocalImage localImage = new LocalImage();
        localImage.setImage(bitmap);
        localImage.setImagePath(path);
        localImage.setTitle(path.substring(path.lastIndexOf("/")+1));
        Log.i("FILENAME",localImage.getTitle());
        localImage.setId(new Random().nextInt());
        localImages.add(localImage);
    }

    private String getRealPathFromUri(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = getContext().getContentResolver().query(contentUri, proj, null,
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof PictureChoosen)
        {
            this.pictureChoosenListener = (PictureChoosen) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.pictureChoosenListener =null;
    }

    public interface PictureChoosen {
        void onPictureChoosenListener(ArrayList<LocalImage> images);
    }

}
