package com.kstefancic.nekretnineinfo.buildingdata;


import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.kstefancic.nekretnineinfo.R;
import com.kstefancic.nekretnineinfo.api.model.Building;
import com.kstefancic.nekretnineinfo.api.model.localDBdto.LocalImage;
import com.kstefancic.nekretnineinfo.helper.DBHelper;
import com.kstefancic.nekretnineinfo.views.PictureGridViewAdapter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import static android.app.Activity.RESULT_OK;
import static com.kstefancic.nekretnineinfo.MainActivity.BUILDING_DATA;

/**
 * Created by user on 15.11.2017..
 */

public class PicturesFragment extends Fragment{

    private static final int REQUEST_IMAGE_CAPTURE = 20;
    private static final int GALLERY_REQUEST = 10;
    private Button btnFinish, btnBrowse;
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
        pictureGridViewAdapter = new PictureGridViewAdapter(getActivity(),R.layout.picture_grid_item, localImages);
        gvPictures.setAdapter(pictureGridViewAdapter);
        this.btnBrowse = layout.findViewById(R.id.frPictures_btnBrowse);
        this.btnBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                String picturesDirectoryPath = pictureDirectory.getPath();

                Uri data = Uri.parse(picturesDirectoryPath);

                intent.setDataAndType(data,"image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, GALLERY_REQUEST);
            }
        });
        this.btnFinish =layout.findViewById(R.id.frPictures_btnFinish);
        this.btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              pictureChoosenListener.onPictureChoosenListener(localImages);
            }
        });
        this.fabCamera = layout.findViewById(R.id.frPictures_fabCamera);
        this.fabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            addImageToList(null,imageBitmap);
            pictureGridViewAdapter.notifyDataSetChanged();
        }
        else if (requestCode == 10 && resultCode == RESULT_OK && data != null) {
            ClipData clipData = data.getClipData();

            for (int i = 0; i < clipData.getItemCount(); i++) {
                ClipData.Item item = clipData.getItemAt(i);
                Uri uri = item.getUri();
                Bitmap bitmap = BitmapFactory.decodeFile(getRealPathFromUri(uri));
                addImageToList(uri, bitmap);
                Log.d("INSERTING IMAGE", uri.toString() + " size: " + localImages.size());
            }
            pictureGridViewAdapter.notifyDataSetChanged();

        }
    }

    private void addImageToList(Uri uri, Bitmap bitmap) {
        LocalImage localImage = new LocalImage();
        localImage.setImage(bitmap);
        //localImage.setImagePath(getRealPathFromUri(uri));
        localImage.setId(new Random().nextInt());
        localImages.add(localImage);
    }

    public String getRealPathFromUri(Uri contentUri) {
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
        void onPictureChoosenListener( ArrayList<LocalImage> images);
    }

}
