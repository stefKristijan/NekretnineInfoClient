package com.kstefancic.nekretnineinfo.buildingdata;


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
import android.widget.ImageButton;
import android.widget.ImageView;

import com.kstefancic.nekretnineinfo.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * Created by user on 15.11.2017..
 */

public class PicturesFragment extends Fragment{

    private Button btnFinish, btnBrowse;
    private ImageButton ibCancel;
    private ImageView ivBuilding;
    private PictureChoosen pictureChoosenListener;
    private  ArrayList<Uri> fileUris;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_pictures,null);
        setUI(layout);
        return layout;
    }

    private void setUI(View layout) {
        this.ivBuilding = layout.findViewById(R.id.frPictures_ivPicture);
        this.btnBrowse = layout.findViewById(R.id.frPictures_btnBrowse);
        this.btnBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                String picturesDirectoryPath = pictureDirectory.getPath();

                Uri data = Uri.parse(picturesDirectoryPath);

                intent.setDataAndType(data,"image/*");

                startActivityForResult(intent, 10);
            }
        });
        this.btnFinish =layout.findViewById(R.id.frPictures_btnFinish);
        this.btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              pictureChoosenListener.onPictureChoosenListener(fileUris);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK && data != null) {
            ClipData clipData = data.getClipData();
            fileUris = new ArrayList<>();
            for (int i = 0; i < clipData.getItemCount(); i++) {
                ClipData.Item item = clipData.getItemAt(i);
                Uri uri = item.getUri();
                Log.d("URI", uri.toString());
                fileUris.add(uri);
            }
            if(fileUris.size()>0){
                Picasso.with(getActivity())
                        .load(fileUris.get(0))
                        .fit()
                        .centerCrop()
                        .into(ivBuilding);
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
        void onPictureChoosenListener( ArrayList<Uri> imageUris);
    }

}
